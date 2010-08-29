package ctvdkip;


import ctvdkip.util.ApplicationLogger;
import ctvdkip.business.Voks;
import ctvdkip.database.navision.NavisionTxtDB;
import ctvdkip.database.voks.AccountingRecordWriter;
import ctvdkip.database.cobaswin.CobasWinDB;
import ctvdkip.gui.SplashWindow;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;


/**
 * @author rbust
 *
 * cobaswin-To-Voks-Data-Konverter
 */
public class CTVDKIP {

	public static void main(String[] p_args) {
		
		//***********************************************************************
        String _argument;							//ARGS from stdin
		//***********************************************************************
        
		//Logging Application Start
		ApplicationLogger.getInstance().getLogger().info("Application started ...");


		new SplashWindow(SplashWindow.class.getResource("kuepper.jpg"),36000);

		// checking input
		if (1 > p_args.length){
			System.out.println("Usage  : Specify ImportType as Parameter of this Programm\n");
			System.out.println("Example: CobasWinToVoks or NavisionToVoks\n");
			System.out.println("System is exiting ...\n");
			ApplicationLogger.getInstance().getLogger().severe(
				"Usage  : Specify ImportType as Parameter of this Programm | "+
				"Example: CobasWinToVoks or NavisionToVoks | "+
				"System is exiting ..."
			);
			System.exit(0);
		}
		
		//get argument
		_argument = p_args[0];
		
		// checking arg
		if (_argument.equals("CobasWinToVoks")) {
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter CobasWinToVoks accepted"
			);
            doingCobasWin();

		}
		else if (_argument.equals("NavisionToVoks")){
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter NavisionToVoks accepted"		
			);
            ApplicationLogger.getInstance().getLogger().info(
				"Parameter NavisionToVoks: Funktion Disabled"
			);
            // doingNavision();
		}
        else if (_argument.equals("GenerateInitialAccountingRecordsFromNavision")){
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateInitialAccountingRecordsFromNavision accepted"
			);
            ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateInitialAccountingRecordsFromNavision: Funktion Disabled"
			);
            //doingGenerateAccountingRecordsFromNavision();
		}
        else if (_argument.equals("GenerateAccountingRecordsFromCobasWin")){
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateAccountingRecordsFromCobasWin accepted"
			);
            doingGenerateAccountingRecordsFromCobasWin();
		}
        else if (_argument.equals("GenerateAccountingRecordsFromNavisionOP")){
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateAccountingRecordsFromNavisionOP accepted"
			);
            ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateAccountingRecordsFromNavisionOP: Funktion Disabled"
			);
            // doingGenerateAccountingRecordsFromNavisionOP();
		}
		else{
			ApplicationLogger.getInstance().getLogger().severe(
				"Could not understand Argument Parameter " + _argument + " ! | "+
				"Programm will exit now ..."
			);
		};//end if
		
		

		
		ApplicationLogger.getInstance().getLogger().info(
				"Applikation is shuting down ..."
		);

        System.exit(0);

	};//end method main()

    private static boolean doingNavision(){

        Voks _voks;				//Voks Database

        _voks = new Voks();

        if(_voks.updateVoksWithNavisionData()){
			ApplicationLogger.getInstance().getLogger().info(
					"updating VoksDB with Navisison Data successfull ... :)"
			);
            return true;
		}
		else{
			ApplicationLogger.getInstance().getLogger().severe(
					"Critical Error :("
			);
            return false;
		}

    }

    private static boolean doingCobasWin(){

            Voks _voks;				//Voks Database

            _voks = new Voks();

            if(_voks.updateVoksWithCobasWinData()){
                ApplicationLogger.getInstance().getLogger().info(
                        "updating VoksDB with cobaswin Data successfull ... :)"
                );
                return true;
            }
            else{
                ApplicationLogger.getInstance().getLogger().severe(
                        "Critical Error :("
                );
                return false;
            }

        }

    private static boolean doingGenerateAccountingRecordsFromNavision(){

        NavisionTxtDB _navisiontextdatabase;
        List _allnavisiondebitors;
        List _allnavisionkreditors;
        AccountingRecordWriter _recordwriter;
        List _accountingrecords;


        _navisiontextdatabase = new NavisionTxtDB();
        _accountingrecords = new LinkedList();
        _recordwriter = new AccountingRecordWriter();

        // checking consistenz of navision files
        if(!_navisiontextdatabase.checkConsistentFiles()){
            ApplicationLogger.getInstance().getLogger().severe("NAVISION FILES NOT CONSISTENT :( ");
            return false;
        }

        /**
         * getting all debitors from navision
         */
        try {
            _allnavisiondebitors = _navisiontextdatabase.getAllDebitors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }

        /**
         * getting all kreditors from navision
         */
        try {
            _allnavisionkreditors = _navisiontextdatabase.getAllKreditors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }

        _accountingrecords.addAll(_recordwriter.generateAccountingRecords(_allnavisiondebitors));
        _accountingrecords.addAll(_recordwriter.generateAccountingRecords(_allnavisionkreditors));

        _recordwriter.writeInitialAccountingRecordsToFile(_accountingrecords);

        return true;
    }

    private static boolean doingGenerateAccountingRecordsFromCobasWin(){

        CobasWinDB _cobaswindb;
        AccountingRecordWriter _recordwriter;
        List _accountingrecords;


        _cobaswindb = new CobasWinDB();
        _accountingrecords = new LinkedList();
        _recordwriter = new AccountingRecordWriter();


        try{
            _accountingrecords = _cobaswindb.getAllNewAcountingRecords();

        }
        catch(SQLException sql_ex){
            ApplicationLogger.getInstance().getLogger().severe("Could not get AccountingRecords from CobasWin :(");
            return false;
        }


        if(!_recordwriter.writeCobasWinAccountingRecordsToFile(_accountingrecords)){
            ApplicationLogger.getInstance().getLogger().severe("Could not save AccountingRecords to File :(");
            return false;
        }
        else{
            //setting status in cobaswin = verarbeitet
            if(!_cobaswindb.setAccountingRecordsVerarbeitet(_accountingrecords)){
                //status changing error :(
                ApplicationLogger.getInstance().getLogger().severe("Could not set AccountingRecords in CobasWin Status = Verarbeitet :(");
                ApplicationLogger.getInstance().getLogger().severe("Do NOT import generated Accounting File !!!!");
            return false;
            }
        }        

        return true;
    }

    private static boolean doingGenerateAccountingRecordsFromNavisionOP(){

        NavisionTxtDB _navisiontextdatabase;
        AccountingRecordWriter _recordwriter;
        List _accountingrecords;


        _navisiontextdatabase = new NavisionTxtDB();
        _accountingrecords = new LinkedList();
        _recordwriter = new AccountingRecordWriter();

        try{
        _accountingrecords.addAll(_navisiontextdatabase.getAllInitizalDebitorOPAccountingRecords());
        }
        catch(SQLException ex){
            ApplicationLogger.getInstance().getLogger().severe("Could NOT get AccountingRecords From Debitor OP :("+ex);
            return false;
        }

        try{
        _accountingrecords.addAll(_navisiontextdatabase.getAllInitizalKreditorOPAccountingRecords());
        }
        catch(SQLException ex){
            ApplicationLogger.getInstance().getLogger().severe("Could NOT get AccountingRecords From Kreditor OP :("+ex);
            return false;
        }
        _recordwriter.writeInitialAccountingRecordsToFile(_accountingrecords);

        return true;
    }




}//end class
