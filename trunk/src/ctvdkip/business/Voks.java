/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ctvdkip.business;


import ctvdkip.database.voks.VoksDB;
import ctvdkip.database.cobaswin.CobasWinDB;
import ctvdkip.database.navision.NavisionTxtDB;
import ctvdkip.database.navision.NavisionDebitorConstants;
import ctvdkip.database.navision.NavisionKreditorConstants;
import ctvdkip.util.ApplicationLogger;

import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

/**
 * @author rbust
 */
public class Voks {


    public Voks() {

    }



    /**
     *
     *
     */
    public boolean updateVoksWithNavisionData() {

        //**********************************************************************
        VoksDB _voksdatabase;
        List _allnavisiondebitors;
        List _allnavisionkreditors;
        NavisionTxtDB _navisiontextdatabase;
        //**********************************************************************

        _voksdatabase = new VoksDB();
        _navisiontextdatabase = new NavisionTxtDB();

        // checking consistenz of navision files
        if(!_navisiontextdatabase.checkConsistentFiles()){
            ApplicationLogger.getInstance().getLogger().severe("NAVISION FILES NOT CONSISTENT :( ");
            return false;
        }

        // checkNavisionPaymentCodes
        if(!this.checkNavisionPaymentCodes()){
            ApplicationLogger.getInstance().getLogger().severe("NAVISION PAYMENT CODE PROBLEM :(");
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

        /**
         * saving all kreditors to voks
         */
        ApplicationLogger.getInstance().getLogger().info("Count Element of List _allnavisionkreditors = " + _allnavisionkreditors.size());

        if (!_voksdatabase.saveKreditor(_allnavisionkreditors)) {
            ApplicationLogger.getInstance().getLogger().severe("Could not saveKreditor to VoksDB :(");
            return false;
        }

        /**
         * saving all debitors to voks
         */
        ApplicationLogger.getInstance().getLogger().info("Count Element of List _allnavisiondebitors = " + _allnavisiondebitors.size());

        if (!_voksdatabase.saveDebitor(_allnavisiondebitors)) {
            ApplicationLogger.getInstance().getLogger().severe("Could not saveDebitors to VoksDB :(");
            return false;
        }


/*
		//checking consistenz of debitore files for EXEL Version

		try{
			if(!_navisionExelDBelDBelDBelDB.checkConsistentFiles()){
				ApplicationLogger.getInstance().getLogger().severe(
						"Debitor files NOT consistent :("
				);
				return false;
			};
		}
		catch (SQLException ex) {
			ApplicationLogger.getInstance().getLogger().severe(
					"Error while checking consistent debitor files :(" +ex
			);
			return false;
		}

		//opening NavisionExelDB
		_navisionExelDBelDBelDBelDB.open();
		try{
			allNavisionData =_navisionExelDBelDBelDBelDB.getAllDebitors();
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Error while getting Data from navision DB :("
			);
			return false;
		}
		try{
			while (allNavisionData.next()){

				_debitorFromNavision.setKundenNr(allNavisionData.getString("Nummer"));
				_debitorFromNavision.setName(allNavisionData.getString("Name"));
				_debitorFromNavision.setZusatz(allNavisionData.getString("Name2"));
				_debitorFromNavision.setStrasse(allNavisionData.getString("Str"));
				_debitorFromNavision.setPlz(allNavisionData.getString("PLZ"));
				_debitorFromNavision.setOrt(allNavisionData.getString("Ort"));
				_debitorFromNavision.setFaxNummer(allNavisionData.getString("Fax"));
				_debitorFromNavision.setLand(allNavisionData.getString("Land"));
				_debitorFromNavision.setTelefon(allNavisionData.getString("Tel"));

				ApplicationLogger.getInstance().getLogger().info(
						"getting all data for debitor = " +
						_debitorFromNavision.getKundenNr() +
						" from File 1"
				);

				remainingData =_navisionExelDBelDBelDBelDB.getRemainingDebitorData(_debitorFromNavision.getKundenNr());
				remainingData.next();

				_debitorFromNavision.setUstID(remainingData.getString("UST-IDNr"));
				_debitorFromNavision.setZahlungsBedingungsCode(remainingData.getString("ZB"));
				_debitorFromNavision.setBankKontoNr(remainingData.getString("Bankkonto"));
				_debitorFromNavision.setBankLeitZahl(remainingData.getString("BLZ"));
				_debitorFromNavision.setBankName(remainingData.getString("Bank"));

				String _tmpstring;
				_tmpstring = new String(remainingData.getString("Bankeinzug"));
				_tmpstring =_tmpstring.trim();

				if (_tmpstring.equalsIgnoreCase("Ja")){
					_debitorFromNavision.setZahlungsverkehr("L");
				}
				else{
					_debitorFromNavision.setZahlungsverkehr("N");
				}


				//_debitorFromCobasWin = _cobaswindatabase.getDebitor(_debitorFromNavision.getKundenNr());

				ApplicationLogger.getInstance().getLogger().info(
						"getting remaining data from debitor = " +
						remainingData.getInt(1)+
						" from File 2"
				);
			}
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Error while getting Remaining Data from navision DB :(" +ex
			);
			return false;
		}
*/

        return true;
    }

    private boolean checkNavisionPaymentCodes(){

        List navisionPaymentCodes = null;         //PaymentCodes from navision
        NavisionTxtDB _navisiontextdatabase;
        VoksDB _voksdatabase;

        _navisiontextdatabase = new NavisionTxtDB();
        _voksdatabase = new VoksDB();

        //get all Debitor Payment Codes from navision
        try {
            navisionPaymentCodes = _navisiontextdatabase.getAllPaymentCodes(NavisionDebitorConstants.DEBITORFILE2);
        } catch (SQLException ex) {
            ApplicationLogger.getInstance().getLogger().severe("Could not get Paymeant Codes from NavisionExelDB. " +
                    "Programm will exit now ...");
            return false;
        }


        // compare navision Debitor Payment codes to Voks Payment codes
        // must be equal
        if (_voksdatabase.compareInputPaymentCodesToVoks(navisionPaymentCodes)) {

            ApplicationLogger.getInstance().getLogger().info("All navision PaymentCodes existing in Voks :)");

        } else {

            ApplicationLogger.getInstance().getLogger().severe("There are PaymentCodes waiting for Import who do not exist in Voks");

            return false;
        }

        //get all Kreditor Payment Codes from navision
        try {
            navisionPaymentCodes = _navisiontextdatabase.getAllPaymentCodes(NavisionKreditorConstants.KREDITORFILE1);
        } catch (SQLException ex) {
            ApplicationLogger.getInstance().getLogger().severe("Could not get Paymeant Codes from NavisionExelDB. " +
                    "Programm will exit now ...");
            return false;
        }

        // compare navision Debitor Payment codes to Voks Payment codes
        // must be equal
        if (_voksdatabase.compareInputPaymentCodesToVoks(navisionPaymentCodes)) {

            ApplicationLogger.getInstance().getLogger().info("All navision PaymentCodes existing in Voks :)");

        } else {

            ApplicationLogger.getInstance().getLogger().severe("There are PaymentCodes waiting for Import who do not exist in Voks");

            return false;
        }
        return true;
    }

    private boolean checkCobasWinPaymentCodes(){

        List cobaswinPaymentCodes = null;			//PaymentCodes from cobaswin
        CobasWinDB _cobaswindatabase;
        VoksDB _voksdatabase;


        _voksdatabase = new VoksDB();
        _cobaswindatabase = new CobasWinDB();

        //get all Payment Codes from cobaswin
        try {
            cobaswinPaymentCodes = _cobaswindatabase.getAllPaymentCodes();
        } catch (SQLException ex) {
            ApplicationLogger.getInstance().getLogger().severe("Could not get Paymeant Codes from CobasWinDB. " +
                    "Programm will exit now ...");
            return false;
        }

        // compare cobaswin Payment codes to Voks Payment codes
        // must be equal
        if (_voksdatabase.compareInputPaymentCodesToVoks(cobaswinPaymentCodes)) {

            ApplicationLogger.getInstance().getLogger().info("All cobaswin PaymentCodes existing in Voks :)");

        } else {

            ApplicationLogger.getInstance().getLogger().severe("There are PaymentCodes waiting for Import who do not exist in Voks");

            return false;
        }
        return true;
    }


    public boolean updateVoksWithCobasWinData(){

        CobasWinDB _cobaswindatabase;
        CobasWin _cobaswinhelper;
        VoksDB  _voksdatabase;
        List _allcobaswindebitors;
        List _allcobaswinkreditors;
        List _splittedvoksrecords[];
        List _allvoksdebitors;
        List _allvokskreditors;

        _allcobaswindebitors = new LinkedList();
        _allcobaswinkreditors =new LinkedList();
        _cobaswindatabase = new CobasWinDB();
        _voksdatabase = new VoksDB();
        _cobaswinhelper = new CobasWin();

        // checkCobasWinPaymentCodes
        ApplicationLogger.getInstance().getLogger().info("checking cobaswin payment codes ...");
        if(!this.checkCobasWinPaymentCodes()){
            ApplicationLogger.getInstance().getLogger().info("checking cobaswin payment codes ... FAILED");
            ApplicationLogger.getInstance().getLogger().severe("COBASWIN PAYMENT CODE PROBLEM :(");
            return false;
        }
        ApplicationLogger.getInstance().getLogger().info("checking cobaswin payment codes ... OK");

        // getting als debitors from voks
        ApplicationLogger.getInstance().getLogger().info("getting all debitors from voks ...");
        try {
            _allvoksdebitors = _voksdatabase.getAllDebitors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().info("getting all debitors from voks ... FAILED");
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }
        ApplicationLogger.getInstance().getLogger().info("getting all debitors from voks ... OK");

        // getting all kreditors from voks
        ApplicationLogger.getInstance().getLogger().info("getting all kreditors from voks ...");
        try {
            _allvokskreditors = _voksdatabase.getAllKreditors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().info("getting all kreditors from voks ... FAILED");
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }
        ApplicationLogger.getInstance().getLogger().info("getting all kreditors from voks ... OK");

        /**
         * getting all debitors from cobaswin
         */
        ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ...");
        try {
            _allcobaswindebitors = _cobaswindatabase.getAllDebitors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ... FAILED");
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }
        ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ... OK");

        /**
         * getting all kreditors from cobaswin
         */
        ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ...");
        try {
            _allcobaswinkreditors = _cobaswindatabase.getAllKreditors();
        } catch (SQLException e) {
            ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ... FAILED");
            ApplicationLogger.getInstance().getLogger().severe("Could not get allnavisiondebitors :(");
            return false;
        }
        ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ... OK");

        // splitting debitos in NEW and UPDATED
        _splittedvoksrecords = _cobaswinhelper.splitIntoUpdateAndInsert(_allcobaswindebitors, _allvoksdebitors);

        // updating debitors
        _voksdatabase.insertDebitorWithoutBankdata(_splittedvoksrecords[0]);

        // inserting new debitors
        _voksdatabase.updateDebitorWithoutBankdata(_splittedvoksrecords[1]);

        // splitting kreditors in NEW and UPDATED
        _splittedvoksrecords = _cobaswinhelper.splitIntoUpdateAndInsert(_allcobaswinkreditors, _allvokskreditors);

        // updating kreditors
        _voksdatabase.insertKreditorWithoutBankdata(_splittedvoksrecords[0]);

        // inserting new kreditors
        _voksdatabase.updateKreditorWithoutBankdata(_splittedvoksrecords[1]);








/*
         // saving all kreditors to voks
        ApplicationLogger.getInstance().getLogger().info("Count Element of List _allnavisionkreditors = " + _allcobaswinkreditors.size());

        if (!_voksdatabase.saveKreditor(_allcobaswinkreditors)) {
            ApplicationLogger.getInstance().getLogger().severe("Could not saveKreditor to VoksDB :(");
            return false;
        }


        // saving all debitors to voks
        ApplicationLogger.getInstance().getLogger().info("Count Element of List _allnavisiondebitors = " + _allcobaswindebitors.size());

        if (!_voksdatabase.saveDebitor(_allcobaswindebitors)) {
            ApplicationLogger.getInstance().getLogger().severe("Could not saveDebitors to VoksDB :(");
            return false;
        }
*/
        return true;

    }

}
