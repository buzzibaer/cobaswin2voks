package ctvdkip;


import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import ctvdkip.business.Voks;
import ctvdkip.database.cobaswin.CobasWinDB;
import ctvdkip.database.voks.AccountingRecordWriter;
import ctvdkip.gui.SplashWindow;
import ctvdkip.util.ApplicationLogger;


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
   
        else if (_argument.equals("GenerateAccountingRecordsFromCobasWin")){
			ApplicationLogger.getInstance().getLogger().info(
				"Parameter GenerateAccountingRecordsFromCobasWin accepted"
			);
            doingGenerateAccountingRecordsFromCobasWin();
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



}//end class
