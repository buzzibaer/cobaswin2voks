/*
 * Created on 09.08.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ctvdkip.business;


import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import ctvdkip.database.cobaswin.CobasWinDB;
import ctvdkip.database.voks.VoksDB;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;
import ctvdkip.util.ApplicationLogger;

/**
 * @author rbust
 */
public class Voks {


    public Voks() {

    }





    private boolean checkCobasWinPaymentCodes(){

        List<Integer> cobaswinPaymentCodes = null;			//PaymentCodes from cobaswin
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
        List<VoksDebitorRecord> _allcobaswindebitors;
        List<VoksKreditorRecord> _allcobaswinkreditors;
        List<List<VoksDebitorRecord>> _splittedDebitorRecords;
        List<List<VoksKreditorRecord>> _splittedKreditorRecords;
        List<VoksDebitorRecord> _allvoksdebitors;
        List<VoksKreditorRecord> _allvokskreditors;

        _allcobaswindebitors = new LinkedList<VoksDebitorRecord>();
        _allcobaswinkreditors =new LinkedList<VoksKreditorRecord>();
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
        _splittedDebitorRecords = _cobaswinhelper.splitIntoUpdateAndInsert(_allcobaswindebitors, _allvoksdebitors);

        // updating debitors
        _voksdatabase.insertDebitorWithoutBankdata(_splittedDebitorRecords.get(0));

        // inserting new debitors
        _voksdatabase.updateDebitorWithoutBankdata(_splittedDebitorRecords.get(1));

        // splitting kreditors in NEW and UPDATED
        _splittedKreditorRecords = _cobaswinhelper.splitIntoUpdateAndInsert(_allcobaswinkreditors, _allvokskreditors);

        // updating kreditors
        _voksdatabase.insertKreditorWithoutBankdata(_splittedKreditorRecords.get(0));

        // inserting new kreditors
        _voksdatabase.updateKreditorWithoutBankdata(_splittedKreditorRecords.get(1));








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
