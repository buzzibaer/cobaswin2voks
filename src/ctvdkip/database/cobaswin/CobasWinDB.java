/*
 * Created on 18.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ctvdkip.database.cobaswin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;
import ctvdkip.util.ApplicationLogger;


/**
 * @author rbust
 * 
 */
public class CobasWinDB {
	
	/**
	 * The database connection object 
	 */
	private Connection dbConnection;
	
	/**
	 * the Database Statement Object
	 */
	private Statement statement;
	
	/**
	 * constructor
	 *
	 */
	public CobasWinDB(){
		
	}
	
	/**
	 * 
	 * opens the dbConnectionnectino to the database
	 *
	 */
	private void open(){
		
		// odbc string
		String _odbcstring = "sun.jdbc.odbc.JdbcOdbcDriver";
		String _dbConnectionString = "jdbc:odbc:cobaswin";
		
		// Loading Driver and opening dbConnectionnection
		try{
			Class.forName(_odbcstring);
		}
		catch(ClassNotFoundException _classNotFoundEx){
			ApplicationLogger.getInstance().getLogger().severe(
					"ODBC String for Voks DB is not valid :("
			);
			ApplicationLogger.getInstance().getLogger().severe(
					"System is shuting down ..."
			);
			System.exit(0);
		}
		
		try{
			dbConnection = DriverManager.getConnection(_dbConnectionString, "dataflex","dataflex");
		}
		catch (SQLException p_sqlex ) {
			
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not open CobasWinDB. " +  p_sqlex.getMessage()
			);
			ApplicationLogger.getInstance().getLogger().severe(
					"System is shuting down ..."
			);
			System.exit(0);
		}
		
		try {
			statement = dbConnection.createStatement();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"DB Statement could not be created :("
			);
		}
	};//end of method open()
	
	/**
	 *  function is closing database 
	 *
	 */
	private boolean close(){
		
		//closing DB Connection
		try{
			dbConnection.close();	
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close VoksDB. Maybe it is already closed?"
			);
			return false;
		};
		
		try{
			statement.close();
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close Statement from Voks DB :( = "+ ex
			);
		}
		return true;
	};
	
	/**
	 * Schliesst die Connectin zur Datenbank.
	 * Diese Methode garantiert das Schliesesn der DB.
	 *
	 */
	protected void finalize(){
		this.close();
	}
	
	/**
	 * BankData Fields will not be filled
	 * @param p_debitornummer
	 * @return
	 */
	public VoksDebitorRecord getDebitor(String p_debitornummer){
		
		//**********************************************************
		VoksDebitorRecord r_debitor;
        String _query = "SELECT " +
               "D." + CobasWinTableDebitor.KUNDENNUMMER + ","+
               "D." + CobasWinTableDebitor.NAME + ","+
               "D." + CobasWinTableDebitor.ZUSATZ + ","+
               "D." + CobasWinTableDebitor.STRASSE + ","+
               "D." + CobasWinTableDebitor.PLZ +","+
               "D." + CobasWinTableDebitor.ORT +","+
               "D." + CobasWinTableDebitor.TELEFON + ","+
               "D." + CobasWinTableDebitor.FAX +","+
               "D." + CobasWinTableDebitor.USTID + ","+
               "D." + CobasWinTableDebitor.ZAHLUNGSBEDINGUNGSCODE +","+
               "D." + CobasWinTableDebitor.BANKKONTONUMMER +","+
                "L."+ CobasWinTableDebitor.LANDKURZNAME +
               " FROM " + CobasWinTableDebitor.DEBITORTABLE + " D, " +
                       CobasWinTableLand.LANDTABLE+" L"+
               " WHERE "+
                CobasWinTableLand.LANDID +
                " = 1 D."+CobasWinTableDebitor.LANDID +
                " = L."+CobasWinTableLand.LANDID +
                " AND NUMMER="+  p_debitornummer.trim();
		ResultSet _rs = null;
		//**********************************************************

        //init
        r_debitor = new VoksDebitorRecord();

		this.open();
		
		try{
			_rs = statement.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the cobaswin DB failed :(" + ex
			);
			ApplicationLogger.getInstance().getLogger().severe(
					"System is shuting down ..."
			);
			System.exit(0);
		}
		try{
			
			while (_rs.next()){
				

				r_debitor.setKundenNr(new String(_rs.getString("NUMMER")));
				r_debitor.setName(new String(_rs.getString("NAAM1")));
				r_debitor.setZusatz(new String(_rs.getString("NAAM2")));
				r_debitor.setStrasse(new String(_rs.getString("ADRES")));
				r_debitor.setPlz(new String(_rs.getString("PKODE_PLAATS")));
				r_debitor.setOrt(new String(_rs.getString("PLAATS")));
				r_debitor.setLand(new String(_rs.getString("ZOEKNAAM")));
				r_debitor.setTelefon(new String(_rs.getString("TELEFOON")));
				r_debitor.setFaxNummer(new String(_rs.getString("FAXNUMMER")));
				r_debitor.setUstID(new String(_rs.getString("BTWNUMMER")));
				r_debitor.setZahlungsBedingungsCode(new String(_rs.getString("BET_KOND")));
				
			};
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not get ResultSet from CobasWIN DB :( = "+ ex
			);
			ApplicationLogger.getInstance().getLogger().severe(
					"System is shuting down ..."
			);
			System.exit(0);
		}
		try{
			_rs.close();
			ApplicationLogger.getInstance().getLogger().info(
					"Closing ResultSet = OK"
			);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close ResultSet from cobaswin DB :( = "+ ex
			);
		}
		
		
		this.close();
		
		return r_debitor;
	} 
	
	public List<Integer> getAllPaymentCodes () throws SQLException {

        // ******************************************************
        // local variables
		String _query;
		ResultSet _rs;
        LinkedList<Integer> r_zbcodesauscobaswin;


        //******************************************************
        //  INIT
        _query = "SELECT DISTINCT "+
                CobasWinTableDebitor.ZAHLUNGSBEDINGUNGSCODE+
                " FROM "+
                CobasWinTableDebitor.DEBITORTABLE;
        _rs = null;
        r_zbcodesauscobaswin = new LinkedList<Integer>();


		this.open();

		try{
			_rs = statement.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the cobaswin DB failed :("  + ex
			);
			throw ex;
		}
		try{
			while (_rs.next()){

				Integer _int;
				_int = new Integer(_rs.getInt("BET_KOND"));
				r_zbcodesauscobaswin.add(_int);

			};
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not get ResultSet from cobaswin DB :( = "+ ex
			);
			throw ex;
		}
		try{
			_rs.close();
			ApplicationLogger.getInstance().getLogger().info(
					"Closing ResultSet = OK"
			);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close ResultSet from cobaswin DB :( = "+ ex
			);
			throw ex;
		}


		this.close();

        ApplicationLogger.getInstance().getLogger().info(
				"Elements of Vector" + r_zbcodesauscobaswin
		);
		return r_zbcodesauscobaswin;
	}// end of method getAllPaymentCodes()

    public List<VoksDebitorRecord> getAllDebitors() throws SQLException{

        // *******************************************************
        // local variavles
		List<VoksDebitorRecord> r_list;
        String _query;
		ResultSet _rs;
		Statement _stmt;
        // *******************************************************

        // *******************************************************
        // init
        r_list = new LinkedList<VoksDebitorRecord>();
        _query = "SELECT"+
                " D."+ CobasWinTableDebitor.KUNDENNUMMER + "," +
                " D."+ CobasWinTableDebitor.NAME + "," +
                " D."+ CobasWinTableDebitor.ZUSATZ + "," +
                " D."+ CobasWinTableDebitor.STRASSE + "," +
                " D."+ CobasWinTableDebitor.PLZ + "," +
                " D."+ CobasWinTableDebitor.ORT + "," +
                " L."+ CobasWinTableDebitor.LANDKURZNAME + "," +
                " D."+ CobasWinTableDebitor.TELEFON + "," +
                " D."+ CobasWinTableDebitor.FAX + "," +
                " D."+ CobasWinTableDebitor.EMAIL + "," +
                " D."+ CobasWinTableDebitor.USTID + "," +
                " D."+ CobasWinTableDebitor.ZAHLUNGSBEDINGUNGSCODE +
                " FROM "+
                CobasWinTableDebitor.DEBITORTABLE +
                " D, "+
                CobasWinTableLand.LANDTABLE +
                " L "+
                " WHERE"+
                " D."+ CobasWinTableDebitor.LANDID +
                " = " +
                "L."+ CobasWinTableLand.LANDID +
                " AND L." + CobasWinTableLand.MANDANT + " = 1";
        // *******************************************************

        this.open();

		try {
			_stmt = dbConnection.createStatement();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"DB Statement could not be created :("
			);
			throw p_sqlex;
		}

		//getting all Data from first navision File
		try{
			_rs = _stmt.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the CobasWin DB failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            VoksDebitorRecord _debitorfromcobaswin;
            _debitorfromcobaswin = new VoksDebitorRecord();

            _debitorfromcobaswin.setKundenNr(_rs.getInt(CobasWinTableDebitor.KUNDENNUMMER));
            _debitorfromcobaswin.setName(_rs.getString(CobasWinTableDebitor.NAME));
            _debitorfromcobaswin.setZusatz(_rs.getString(CobasWinTableDebitor.ZUSATZ));
            _debitorfromcobaswin.setStrasse(_rs.getString(CobasWinTableDebitor.STRASSE));
            _debitorfromcobaswin.setPlz(_rs.getString(CobasWinTableDebitor.PLZ));
            _debitorfromcobaswin.setOrt(_rs.getString(CobasWinTableDebitor.ORT));
            _debitorfromcobaswin.setLand(_rs.getString(CobasWinTableLand.LANDKURZNAME));
            _debitorfromcobaswin.setTelefon(_rs.getString(CobasWinTableDebitor.TELEFON));
            _debitorfromcobaswin.setFaxNummer(_rs.getString(CobasWinTableDebitor.FAX));
            _debitorfromcobaswin.setEmail(_rs.getString(CobasWinTableDebitor.EMAIL));
            _debitorfromcobaswin.setUstID(_rs.getString(CobasWinTableDebitor.USTID));
            _debitorfromcobaswin.setZahlungsBedingungsCode(_rs.getString(CobasWinTableDebitor.ZAHLUNGSBEDINGUNGSCODE));

            _debitorfromcobaswin.setZahlungsverkehr("N");


            r_list.add(_debitorfromcobaswin);
        }
		

        this.close();

		return r_list;

    }

    public List<VoksKreditorRecord> getAllKreditors() throws SQLException{

        // *******************************************************
        // local variavles
		List<VoksKreditorRecord> r_list;
        String _query;
		ResultSet _rs;
		Statement _stmt;
        // *******************************************************

        // *******************************************************
        // init
        r_list = new LinkedList<VoksKreditorRecord>();
        _query = "SELECT"+
                " D."+ CobasWinTableKreditor.LIEFERANTENNUMMER + "," +
                " D."+ CobasWinTableKreditor.NAME + "," +
                " D."+ CobasWinTableKreditor.ZUSATZ + "," +
                " D."+ CobasWinTableKreditor.STRASSE + "," +
                " D."+ CobasWinTableKreditor.PLZ + "," +
                " D."+ CobasWinTableKreditor.ORT + "," +
                " L."+ CobasWinTableKreditor.LANDKURZNAME + "," +
                " D."+ CobasWinTableKreditor.TELEFON + "," +
                " D."+ CobasWinTableKreditor.FAX + "," +
                " D."+ CobasWinTableKreditor.EMAIL + "," +                
                " D."+ CobasWinTableKreditor.ZAHLUNGSBEDINGUNGSCODE +
                " FROM "+
                CobasWinTableKreditor.KREDITORTABLE +
                " D, "+
                CobasWinTableLand.LANDTABLE +
                " L "+
                " WHERE"+
                " D."+ CobasWinTableKreditor.LANDID +
                 " = " +
                "L."+ CobasWinTableLand.LANDID +
                " AND L." + CobasWinTableLand.MANDANT + " = 1";
        // *******************************************************

        this.open();

		try {
			_stmt = dbConnection.createStatement();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"DB Statement could not be created :("
			);
			throw p_sqlex;
		}

		//getting all Data from first navision File
		try{
			_rs = _stmt.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the navision DB failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            VoksKreditorRecord _kreditorfromcobaswin;
            _kreditorfromcobaswin = new VoksKreditorRecord();

            _kreditorfromcobaswin.setLieferantenNr(_rs.getInt(CobasWinTableKreditor.LIEFERANTENNUMMER));
            _kreditorfromcobaswin.setName(_rs.getString(CobasWinTableKreditor.NAME));
            _kreditorfromcobaswin.setZusatz(_rs.getString(CobasWinTableKreditor.ZUSATZ));
            _kreditorfromcobaswin.setStrasse(_rs.getString(CobasWinTableKreditor.STRASSE));
            _kreditorfromcobaswin.setPlz(_rs.getString(CobasWinTableKreditor.PLZ));
            _kreditorfromcobaswin.setOrt(_rs.getString(CobasWinTableKreditor.ORT));
            _kreditorfromcobaswin.setLand(_rs.getString(CobasWinTableLand.LANDKURZNAME));
            _kreditorfromcobaswin.setTelefon(_rs.getString(CobasWinTableKreditor.TELEFON));
            _kreditorfromcobaswin.setFaxNummer(_rs.getString(CobasWinTableKreditor.FAX));
            _kreditorfromcobaswin.setEmail(_rs.getString(CobasWinTableKreditor.EMAIL));
            _kreditorfromcobaswin.setZahlungsBedingungsCode(_rs.getString(CobasWinTableKreditor.ZAHLUNGSBEDINGUNGSCODE));

            _kreditorfromcobaswin.setZahlungsverkehr("N");


            r_list.add(_kreditorfromcobaswin);
        }



		ApplicationLogger.getInstance().getLogger().info(
				"Quering navision DB for AllData = OK"
		);

        this.close();

		return r_list;


    }

    public List<AccountingRecord> getAllNewAcountingRecords() throws SQLException {

        //local variables
        final List<AccountingRecord> r_list = new LinkedList<AccountingRecord>();
		final ResultSet _rs;
		final Statement _stmt;

		final String _query = "SELECT "+
                CobasWinTableBoeking.RECORDNUMMER + "," +
                CobasWinTableBoeking.BELEGDATUM + "," +
                CobasWinTableBoeking.BELEGNUMMER + "," +
                CobasWinTableBoeking.BELEGTEXT + "," +
                CobasWinTableBoeking.KONTO + "," +
                CobasWinTableBoeking.GEGENKONTO + "," +
                CobasWinTableBoeking.BETRAG + "," +
                CobasWinTableBoeking.UMSATZSTEUER + "," +
                CobasWinTableBoeking.SKONTO + "," +
                CobasWinTableBoeking.KOSTENSTELLE + "," +
                CobasWinTableBoeking.KOSTVONKONTO + "," +
                CobasWinTableBoeking.KUNDENLIEFERANTENNUMMER + "," +
                CobasWinTableBoeking.VERARBEITET +
                " FROM " +
                CobasWinTableBoeking.BOEKINGTABLE+
                " WHERE " +
                CobasWinTableBoeking.VERARBEITET +
                " = " +
                "'"+ CobasWinTableBoeking.VERARBEITET_NO + "'" ;

        // *******************************************************

        this.open();

		try {
			_stmt = dbConnection.createStatement();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"DB Statement could not be created :("
			);
			throw p_sqlex;
		}

		//getting all Data from first navision File
		try{
			_rs = _stmt.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the cobaswin DB failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            final AccountingRecord _tmprecord = new AccountingRecord();

            _tmprecord.setRecordNummer(_rs.getString(CobasWinTableBoeking.RECORDNUMMER));
            _tmprecord.setBelegDatum(_rs.getDate(CobasWinTableBoeking.BELEGDATUM));
            _tmprecord.setBelegNummer(_rs.getString(CobasWinTableBoeking.BELEGNUMMER));
            _tmprecord.setBelegText(_rs.getString(CobasWinTableBoeking.BELEGTEXT));
            _tmprecord.setKonto(_rs.getString(CobasWinTableBoeking.KONTO));
            _tmprecord.setGegenKonto(_rs.getString(CobasWinTableBoeking.GEGENKONTO));
            _tmprecord.setBetrag(_rs.getFloat(CobasWinTableBoeking.BETRAG));
            _tmprecord.setUstSchluessel(_rs.getString(CobasWinTableBoeking.UMSATZSTEUER));
            _tmprecord.setSkonto(_rs.getFloat(CobasWinTableBoeking.SKONTO));
            _tmprecord.setKostenstelle(_rs.getString(CobasWinTableBoeking.KOSTENSTELLE));
            _tmprecord.setKosteenstelleVomKonto(_rs.getString(CobasWinTableBoeking.KOSTVONKONTO));
            _tmprecord.setKundenLieferantenNummer(_rs.getString(CobasWinTableBoeking.KUNDENLIEFERANTENNUMMER));
            _tmprecord.setVerarbeitet(_rs.getString(CobasWinTableBoeking.VERARBEITET));

            r_list.add(_tmprecord);
        }



		ApplicationLogger.getInstance().getLogger().info(
				"Quering CobasWin DB for AllNewAccountingRecords = OK"
		);

        this.close();

        return r_list;
    }

    public boolean setAccountingRecordsVerarbeitet(List<AccountingRecord> p_records) {

        //local variables
        String _query;
		Statement _stmt;

        this.open();
/*
No Support for Transaktion Logging
        //Transaktinoskontolle
            try{
               dbConnection.setAutoCommit(false);
            }
            catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                           "SET AUTOCOMMIT = FALSE ... FAILED :( "   + ex
                   );
                return false;
            }
*/
        for(final AccountingRecord _tmprecord : p_records) {

            _query = "UPDATE "+
                CobasWinTableBoeking.BOEKINGTABLE +
                " SET " +
                CobasWinTableBoeking.VERARBEITET + " = " +
                "'" + CobasWinTableBoeking.VERARBEITET_YES + "'" +
//                "'" + CobasWinTableBoeking.VERARBEITET_NO + "'" + //@TODO VERARBEITET YES/NO
                " WHERE " +
                CobasWinTableBoeking.RECORDNUMMER  +
                " = " + _tmprecord.getRecordNummer()
                 ;

            // *******************************************************



            try {
                _stmt = dbConnection.createStatement();
            }
            catch(SQLException p_sqlex){
                ApplicationLogger.getInstance().getLogger().severe(
                        "DB Statement could not be created :("
                );
                return false;
            }

            //getting all Data from first navision File
            try{
                _stmt.executeUpdate(_query);
            }
            catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                        "Query the cobaswin DB failed :(" + ex
                );
                ApplicationLogger.getInstance().getLogger().severe(
                        "Query = " + _query
                );
                return false;
            }

        }
/*
        try {
                dbConnection.commit();
            } catch (SQLException e) {
                ApplicationLogger.getInstance().getLogger().severe(
                           "COMMITTIN TO DATABASE ... FAILED :("
                   );
            }

            try {
                dbConnection.setAutoCommit(true);
            } catch (SQLException e) {
                ApplicationLogger.getInstance().getLogger().severe(
                           "SET AUTO COMMIT = TRUE ... FAILED :("
                   );
            }
*/
        
        this.close();

        return true;

    }



}
