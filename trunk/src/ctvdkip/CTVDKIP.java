package ctvdkip;

import java.sql.SQLException;
import java.util.List;

import ctvdkip.business.CobasWin;
import ctvdkip.business.Voks;
import ctvdkip.database.agenda.AgendaRecordWriter;
import ctvdkip.database.cobaswin.CobasWinDB;
import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.AccountingRecordWriter;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;
import ctvdkip.gui.SplashWindow;
import ctvdkip.util.ApplicationLogger;

/**
 * @author rbust
 *
 *         cobaswin-To-Voks-Data-Konverter
 */
public class CTVDKIP {

   public static void main(final String[] p_args) {

      //Logging Application Start
      ApplicationLogger.getInstance().getLogger().info("Application started ...");

      new SplashWindow(SplashWindow.class.getResource("kuepper.jpg"), 36000);

      // checking input
      if (p_args.length < 1 || p_args.length > 2) {
         System.out.println("Usage  : Specify ImportType as Parameter of this Programm\n");
         System.out.println("Example: CobasWinToVoks or GenerateAgendaAccountingRecordsFromCobasWin\n");
         System.out.println("HIC SUNT DRACONIS: Specifying a second parameter means using 7->6 account number migration\n");
         System.out.println("System is exiting ...\n");
         ApplicationLogger
         .getInstance()
         .getLogger()
         .severe(
               "Usage  : Specify ImportType as Parameter of this Programm | "
                     + "Example: CobasWinToVoks or GenerateAgendaAccountingRecordsFromCobasWin | "
                     + "HIC SUNT DRACONIS: Specifying a second parameter means using 7->6 account number migration | "
                     + "System is exiting ...");
         System.exit(0);
      }

      //get argument
      final String _argument = p_args[0];
      final boolean doAccountNumberDownsizing = p_args.length >= 2;

      if (doAccountNumberDownsizing) {
         ApplicationLogger.getInstance().getLogger().info("Using 7->6 account number migration!");
      } else {
         ApplicationLogger.getInstance().getLogger().info("Not using account number migration!");
      }

      // checking arg
      if (_argument.equals("CobasWinToVoks")) {
         ApplicationLogger.getInstance().getLogger().info("Parameter CobasWinToVoks accepted");
         CTVDKIP.doingCobasWin(doAccountNumberDownsizing);

      }

      else if (_argument.equals("GenerateAccountingRecordsFromCobasWin")) {
         ApplicationLogger.getInstance().getLogger().info("Parameter GenerateAccountingRecordsFromCobasWin accepted");
         CTVDKIP.doingGenerateAccountingRecordsFromCobasWin(doAccountNumberDownsizing);
      } else if (_argument.equals("GenerateAgendaAccountingRecordsFromCobasWin")) {
         ApplicationLogger.getInstance().getLogger().info("Parameter GenerateAgendaAccountingRecordsFromCobasWin accepted");
         CTVDKIP.doingGenerateAgendaAccountingRecordsFromCobasWin();
      } else if (_argument.equals("GenerateAgendaPersonAccountsFromCobasWin")) {
         ApplicationLogger.getInstance().getLogger().info("Parameter GenerateAgendaPersonAccountsFromCobasWin accepted");
         CTVDKIP.doingGenerateAgendaPersonAccountsFromCobasWin();
      } else {
         ApplicationLogger.getInstance().getLogger()
         .severe("Could not understand Argument Parameter " + _argument + " ! | " + "Programm will exit now ...");
      }
      ;//end if

      ApplicationLogger.getInstance().getLogger().info("Applikation is shuting down ...");

      System.exit(0);

   };//end method main()

   private static boolean doingCobasWin(final boolean doAccountNumberDownsizing) {

      final Voks _voks = new Voks(); //Voks Database

      if (_voks.updateVoksWithCobasWinData(doAccountNumberDownsizing)) {
         ApplicationLogger.getInstance().getLogger().info("updating VoksDB with cobaswin Data successfull ... :)");
         return true;
      } else {
         ApplicationLogger.getInstance().getLogger().severe("Critical Error :(");
         return false;
      }

   }

   private static boolean doingGenerateAccountingRecordsFromCobasWin(final boolean doAccountNumberDownsizing) {

      final CobasWinDB _cobaswindb = new CobasWinDB();
      final AccountingRecordWriter _recordwriter = new AccountingRecordWriter();
      final List<AccountingRecord> _accountingrecords;

      try {
         _accountingrecords = _cobaswindb.getAllNewAcountingRecords();
      } catch (final SQLException sql_ex) {
         ApplicationLogger.getInstance().getLogger().severe("Could not get AccountingRecords from CobasWin :(");
         return false;
      }

      if (doAccountNumberDownsizing) {
         CobasWin.migrateAccountRecordsFrom7to6Digits(_accountingrecords);
         ApplicationLogger.getInstance().getLogger().info("Migrated all account numbers from 7 to 6 digits");
      }

      if (!_recordwriter.writeCobasWinAccountingRecordsToFile(_accountingrecords)) {
         ApplicationLogger.getInstance().getLogger().severe("Could not save AccountingRecords to File :(");
         return false;
      } else {
         //setting status in cobaswin = verarbeitet
         if (!_cobaswindb.setAccountingRecordsVerarbeitet(_accountingrecords)) {
            //status changing error :(
            ApplicationLogger.getInstance().getLogger().severe("Could not set AccountingRecords in CobasWin Status = Verarbeitet :(");
            ApplicationLogger.getInstance().getLogger().severe("Do NOT import generated Accounting File !!!!");
            return false;
         }
      }

      return true;
   }

   private static boolean doingGenerateAgendaAccountingRecordsFromCobasWin() {
      final CobasWinDB cobaswinDb = new CobasWinDB();
      final AgendaRecordWriter recordwriter = new AgendaRecordWriter();

      final List<AccountingRecord> accountingRecords;
      try {
         accountingRecords = cobaswinDb.getAllNewAcountingRecords();
      } catch (final SQLException sql_ex) {
         ApplicationLogger.getInstance().getLogger().severe("Could not get AccountingRecords from CobasWin :(");
         return false;
      }

      CobasWin.migrateAccountRecordsFrom4To6Digits(accountingRecords);

      if (!recordwriter.writeAgendaAccountingRecordsToFile(accountingRecords)) {
         ApplicationLogger.getInstance().getLogger().severe("Could not save Agenda AccountingRecords to File :(");
         return false;
      } else {
         //setting status in cobaswin = verarbeitet
         if (!cobaswinDb.setAccountingRecordsVerarbeitet(accountingRecords)) {
            //status changing error :(
            ApplicationLogger.getInstance().getLogger().severe("Could not set AccountingRecords in CobasWin Status = Verarbeitet :(");
            ApplicationLogger.getInstance().getLogger().severe("Do NOT import generated Accounting File !!!!");
            return false;
         }
      }

      return true;

   }

   private static boolean doingGenerateAgendaPersonAccountsFromCobasWin() {
      final CobasWinDB cobaswinDb = new CobasWinDB();
      final AgendaRecordWriter recordwriter = new AgendaRecordWriter();

      final List<VoksDebitorRecord> debitors;
      final List<VoksKreditorRecord> kreditors;

      /**
       * getting all debitors from cobaswin
       */
      ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ...");
      try {
         debitors = cobaswinDb.getAllDebitors();
      } catch (final SQLException e) {
         ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ... FAILED");
         ApplicationLogger.getInstance().getLogger().severe("Could not get allcobaswindebitors :(");
         return false;
      }
      ApplicationLogger.getInstance().getLogger().info("getting all debitors from cobaswin ... OK");

      /**
       * getting all kreditors from cobaswin
       */
      ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ...");
      try {
         kreditors = cobaswinDb.getAllKreditors();
      } catch (final SQLException e) {
         ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ... FAILED");
         ApplicationLogger.getInstance().getLogger().severe("Could not get allcobaswinkreditors :(");
         return false;
      }
      ApplicationLogger.getInstance().getLogger().info("getting all kreditors from cobaswin ... OK");

      ApplicationLogger.getInstance().getLogger().info("Writing debitors and kreditors to file ...");
      if (!recordwriter.writeAgendaPersonAccountsToFile(debitors, kreditors)) {
         ApplicationLogger.getInstance().getLogger().severe("Could not save Agenda person accounts to File :(");
         return false;
      }
      ApplicationLogger.getInstance().getLogger().info("Writing debitors and kreditors to file ... OK");

      return true;
   }

}//end class
