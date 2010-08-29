package ctvdkip.database.voks;

import ctvdkip.util.ApplicationLogger;

import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;


/**
 *
 * Voks Database Acces Class
 * 
 * @author rbust
 */
public class VoksDB {
	
	/**
	 * The database connection object
	 */
	private Connection dbConnection;
	
	/**
	 * the Database Statement Object
	 */
	private Statement statement;
	
	/**
	 * dbConnectionstructor for Voks DB
	 *
	 */
	public VoksDB(){
	};
	
	
	public List getAllVoksPaymentCodes(){

        //local variables
		String _query;
		ResultSet _rs;
		List _zahlbedausvoks;

        //init
         _zahlbedausvoks = new LinkedList();
        _rs = null;
        _query = "SELECT DISTINCT "+
                VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE +
                " FROM " +
                VoksTableZahlbed.ZAHLUNGSBEDINGUNGEN_TABLE;

		this.open();
		
		try{
			_rs = statement.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the Voks DB failed :("
			);
			ApplicationLogger.getInstance().getLogger().severe(
					"System is shuting down ..."
			);
			System.exit(0);
		}
		try{
			while (_rs.next()){
				
				Integer _int;
				_int = new Integer(_rs.getInt(VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE));
/*
                ApplicationLogger.getInstance().getLogger().info(
						"ZB Value = "+ _int
				);
*/
				_zahlbedausvoks.add(_int);
				
			};
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not get ResultSet from Voks DB :( = "+ ex
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
					"Could not close ResultSet from Voks DB :( = "+ ex
			);
		}
		
		
		this.close();
		
		return _zahlbedausvoks;
	}// end of method getAllVoksPaymentCodes()
	
	
	
	/**
	 * 
	 * opens the dbConnectionnectino to the database
	 */
	private void open(){
		
		// odbc string
		String _odbcstring = "sun.jdbc.odbc.JdbcOdbcDriver";
		String _dbConnectionString = "jdbc:odbc:VoksDB";
		
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
			dbConnection = DriverManager.getConnection(_dbConnectionString);
		}
		catch (SQLException p_sqlex ) {
			
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not open VoksDB. DB is configured for exclusivly and maybe still open by Voks?"
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

     public boolean saveDebitor(List p_debitoren){


         this.open();

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


         for (Iterator eone = p_debitoren.iterator(); eone.hasNext();){

             VoksDebitorRecord _tmpdebitor;
             int _id;

             _tmpdebitor = (VoksDebitorRecord) eone.next();

             String _query;
                 _query = "INSERT INTO " +
                        VoksTableAdress.ADDRESS_TABLE +
                        " ("+
                        VoksTableAdress.KUNDENNUMMER       + "," +
                        VoksTableAdress.NAME               + "," +
                        VoksTableAdress.ZUSATZ             + "," +
                        VoksTableAdress.STRASSE            + "," +
                        VoksTableAdress.LAND               + "," +
                        VoksTableAdress.PLZ                + "," +
                        VoksTableAdress.ORT                + "," +
                        VoksTableAdress.TELEFON            + "," +
                        VoksTableAdress.FAX                + "," +
                        VoksTableAdress.EMAIL              + "," +
                        VoksTableAdress.BANKKONTONUMMER    + "," +
                        VoksTableAdress.BANKLEITZAHL       + "," +
                        VoksTableAdress.BANKNAME           + "," +
                        VoksTableAdress.BANKEINZUG         +
                        ") VALUES (" +
                        _tmpdebitor.getKundenNr()                     + "," +
                        "'" + _tmpdebitor.getName()   + "'"           + "," +
                        "'" + _tmpdebitor.getZusatz() + "'"           + "," +
                        "'" + _tmpdebitor.getStrasse() + "'"          + "," +
                        "'" + _tmpdebitor.getLand() + "'"             + "," +
                        "'" + _tmpdebitor.getPLZ() + "'"              + "," +
                        "'" + _tmpdebitor.getOrt() + "'"              + "," +
                        "'" + _tmpdebitor.getTelefon() + "'"          + "," +
                        "'" + _tmpdebitor.getFax() + "'"              + "," +
                        "'" + _tmpdebitor.getEmail() + "'"              + "," +
                        "'" + _tmpdebitor.getBankKontoNr() + "'"      + "," +
                        "'" + _tmpdebitor.getBankleitzahl() + "'"     + "," +
                        "'" + _tmpdebitor.getBankname() + "'"         + "," +
                        "'" + _tmpdebitor.getZahlungsverkehr() + "'"  + ")";

             try{
			    statement.executeUpdate(_query);

             }
             catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                        "UPDATE VFAdress failed :("   + ex
                );
                 ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = "   + _query
                );
                return false;
             }

             _query = "SELECT ID FROM "+
                         VoksTableAdress.ADDRESS_TABLE +
                         " WHERE "+
                         VoksTableAdress.KUNDENNUMMER +
                         " = " +
                         "'" + _tmpdebitor.getKundenNr() + "'";

             try{
                 ResultSet _rs;

                 _rs = statement.executeQuery(_query);
                  _rs.next();
                 _id =  _rs.getInt("ID");

             }
             catch(SQLException ex){
                 ApplicationLogger.getInstance().getLogger().severe(
                        "SELECT ID FROM VFKunden failed :(" + ex
                );
                 ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = " + _query
                );
                return false;

             }

             _query = "INSERT INTO "+
                         VoksTableKunden.KUNDEN_TABLE +
                         " (" +
                         VoksTableAdress.KUNDENNUMMER   + "," +
                         VoksTableAdress.ADRESS_ID   + "," +
                         VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE   + "," +
                         VoksTableKunden.USTID  + "," +
                         VoksTableKunden.SAMMELKONTODEBITOR   + ")" +
                         " VALUES (" +
                         _tmpdebitor.getKundenNr() + "," +
                         _id + "," +
                         "'" + _tmpdebitor.getZahlungsBedingungsCode() + "'" + ","+
                         "'" + _tmpdebitor.getUstID() + "'" + "," +
                         VoksTableKunden.SAMMELKONTO + ")";

             try{
                 statement.executeUpdate(_query);


             }
             catch(SQLException ex){
                 ApplicationLogger.getInstance().getLogger().severe(
                        "INSERT INTO " + VoksTableKunden.KUNDEN_TABLE + " failed :(" + ex
                );
                ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = " + _query
                );
                return false;

             }


         }

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

         this.close();
         return true;
    }


         public boolean saveKreditor(List p_kreditoren){


         this.open();

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


         for (Iterator eone = p_kreditoren.iterator(); eone.hasNext();){

             VoksKreditorRecord _tmpkreditor;
             int _id;

             _tmpkreditor = (VoksKreditorRecord) eone.next();

             String _query;
                 _query = "INSERT INTO " +
                        VoksTableAdress.ADDRESS_TABLE +
                        " ("+
                        VoksTableAdress.LIEFERANTENNUMMER  + "," +
                        VoksTableAdress.NAME               + "," +
                        VoksTableAdress.ZUSATZ             + "," +
                        VoksTableAdress.STRASSE            + "," +
                        VoksTableAdress.LAND               + "," +
                        VoksTableAdress.PLZ                + "," +
                        VoksTableAdress.ORT                + "," +
                        VoksTableAdress.TELEFON            + "," +
                        VoksTableAdress.FAX                + "," +
                        VoksTableAdress.EMAIL              + "," +
                        VoksTableAdress.BANKKONTONUMMER    + "," +
                        VoksTableAdress.BANKLEITZAHL       + "," +
                        VoksTableAdress.BANKNAME           + "," +
                        VoksTableAdress.BANKEINZUG         +
                        ") VALUES (" +
                        _tmpkreditor.getLieferantenNr()                     + "," +
                        "'" + _tmpkreditor.getName()   + "'"           + "," +
                        "'" + _tmpkreditor.getZusatz() + "'"           + "," +
                        "'" + _tmpkreditor.getStrasse() + "'"          + "," +
                        "'" + _tmpkreditor.getLand() + "'"             + "," +
                        "'" + _tmpkreditor.getPLZ() + "'"              + "," +
                        "'" + _tmpkreditor.getOrt() + "'"              + "," +
                        "'" + _tmpkreditor.getTelefon() + "'"          + "," +
                        "'" + _tmpkreditor.getFax() + "'"              + "," +
                        "'" + _tmpkreditor.getEmail() + "'"            + "," +
                        "'" + _tmpkreditor.getBankKontoNr() + "'"      + "," +
                        "'" + _tmpkreditor.getBankleitzahl() + "'"     + "," +
                        "'" + _tmpkreditor.getBankname() + "'"         + "," +
                        "'" + _tmpkreditor.getZahlungsverkehr() + "'"  + ")";

             try{
			    statement.executeUpdate(_query);

             }
             catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                        "UPDATE "+ VoksTableAdress.ADDRESS_TABLE +" failed :("   + ex
                );
                 ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = "   + _query
                );
                return false;
             }

             _query = "SELECT ID FROM "+
                         VoksTableAdress.ADDRESS_TABLE +
                         " WHERE "+
                         VoksTableAdress.LIEFERANTENNUMMER +
                         " = " +
                         "'" + _tmpkreditor.getLieferantenNr() + "'";

             try{
                 ResultSet _rs;

                 _rs = statement.executeQuery(_query);
                  _rs.next();
                 _id =  _rs.getInt("ID");

             }
             catch(SQLException ex){
                 ApplicationLogger.getInstance().getLogger().severe(
                        "SELECT ID FROM " + VoksTableAdress.ADDRESS_TABLE +" failed :(" + ex
                );
                 ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = " + _query
                );
                return false;

             }

             _query = "INSERT INTO "+
                         VoksTableLieferanten.LIEFERANTEN_TABLE +
                         " (" +
                         VoksTableLieferanten.LIEFERANTENNUMMER   + "," +
                         VoksTableLieferanten.ADRESSID   + "," +
                         VoksTableLieferanten.ZAHLUNGSBEDINGUNGSCODE   + "," +
                         VoksTableLieferanten.SAMMELKONTOKREDITOR + "," +
                         VoksTableLieferanten.EIGENEKUNDENNUMMER   + ")" +
                         " VALUES (" +
                         _tmpkreditor.getLieferantenNr() + "," +
                         _id + "," +
                         "'" + _tmpkreditor.getZahlungsBedingungsCode() + "'" + ","+
                        "'" + VoksTableLieferanten.SAMMELKONTO + "'" + ","+
                         "'" + _tmpkreditor.getEigeneKundenNr() + "'" + ")";

             try{
                 statement.executeUpdate(_query);


             }
             catch(SQLException ex){
                 ApplicationLogger.getInstance().getLogger().severe(
                        "INSERT INTO " + VoksTableLieferanten.LIEFERANTEN_TABLE + " failed :(" + ex
                );
                ApplicationLogger.getInstance().getLogger().severe(
                        "QUERY = " + _query
                );
                return false;

             }


         }

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

         this.close();
         return true;
    }

    /**
     * method is checking the given payment codes against the VoksDB.
     * import should be canceled if not equal
     * voks zahlungsbedingungen could be more = OK
     */
    public boolean compareInputPaymentCodesToVoks(List p_paymentcodes) {


        List _vokspaymentcodes;
        Integer _compare;
        boolean found;

        found = false;


        _vokspaymentcodes = this.getAllVoksPaymentCodes();



        for (Iterator enum_payment = p_paymentcodes.iterator(); enum_payment.hasNext();) {
            _compare = (Integer) enum_payment.next();


            found = false;

            for (Iterator enum = _vokspaymentcodes.iterator(); enum.hasNext();) {

                Integer _voks_integer = (Integer) enum.next();

                if (0 == _compare.compareTo(_voks_integer)) {

                    found = true;
                    break;
                }

            }

            if (!found) {

                ApplicationLogger.getInstance().getLogger().severe("NOT FOUND : compare value ==> " +
                        _compare);
                return false;
            }

            found = false;
        }

        return true;

    };//end of method compareInputPaymentCodesToVoks()

    public boolean insertDebitorWithoutBankdata(List p_debitoren){

        int count = 0;

        ApplicationLogger.getInstance().getLogger().info("inserting voks debitors without bankdata ...");

        this.open();

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


        for (Iterator eone = p_debitoren.iterator(); eone.hasNext();){

            count ++;

            ApplicationLogger.getInstance().getLogger().info("inserting voks debitors " + count + " / " + p_debitoren.size());

            VoksDebitorRecord _tmpdebitor;
            int _id;

            _tmpdebitor = (VoksDebitorRecord) eone.next();

            String _query;
                _query = "INSERT INTO " +
                       VoksTableAdress.ADDRESS_TABLE +
                       " ("+
                       VoksTableAdress.KUNDENNUMMER       + "," +
                       VoksTableAdress.NAME               + "," +
                       VoksTableAdress.ZUSATZ             + "," +
                       VoksTableAdress.STRASSE            + "," +
                       VoksTableAdress.LAND               + "," +
                       VoksTableAdress.PLZ                + "," +
                       VoksTableAdress.ORT                + "," +
                       VoksTableAdress.TELEFON            + "," +
                       VoksTableAdress.FAX                + "," +
                       VoksTableAdress.EMAIL              + "," +
                       VoksTableAdress.BANKEINZUG         +
                       ") VALUES (" +
                       _tmpdebitor.getKundenNr()                     + "," +
                       "'" + _tmpdebitor.getName()   + "'"           + "," +
                       "'" + _tmpdebitor.getZusatz() + "'"           + "," +
                       "'" + _tmpdebitor.getStrasse() + "'"          + "," +
                       "'" + _tmpdebitor.getLand() + "'"             + "," +
                       "'" + _tmpdebitor.getPLZ() + "'"              + "," +
                       "'" + _tmpdebitor.getOrt() + "'"              + "," +
                       "'" + _tmpdebitor.getTelefon() + "'"          + "," +
                       "'" + _tmpdebitor.getFax() + "'"              + "," +
                       "'" + _tmpdebitor.getEmail() + "'"            + "," +
                       "'" + _tmpdebitor.getZahlungsverkehr() + "'"  + ")";

            try{
               statement.executeUpdate(_query);

            }
            catch(SQLException ex){
               ApplicationLogger.getInstance().getLogger().severe(
                       "UPDATE VFAdress failed :("   + ex
               );
                ApplicationLogger.getInstance().getLogger().severe(
                       "QUERY = "   + _query
               );
               return false;
            }

            _query = "SELECT ID FROM "+
                        VoksTableAdress.ADDRESS_TABLE +
                        " WHERE "+
                        VoksTableAdress.KUNDENNUMMER +
                        " = " +
                        "'" + _tmpdebitor.getKundenNr() + "'";

            try{
                ResultSet _rs;

                _rs = statement.executeQuery(_query);
                 _rs.next();
                _id =  _rs.getInt("ID");

            }
            catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                       "SELECT ID FROM VFKunden failed :(" + ex
               );
                ApplicationLogger.getInstance().getLogger().severe(
                       "QUERY = " + _query
               );
               return false;

            }

            _query = "INSERT INTO "+
                        VoksTableKunden.KUNDEN_TABLE +
                        " (" +
                        VoksTableAdress.KUNDENNUMMER   + "," +
                        VoksTableAdress.ADRESS_ID   + "," +
                        VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE   + "," +
                        VoksTableKunden.USTID  + "," +
                        VoksTableKunden.SAMMELKONTODEBITOR   + ")" +
                        " VALUES (" +
                        _tmpdebitor.getKundenNr() + "," +
                        _id + "," +
                        "'" + _tmpdebitor.getZahlungsBedingungsCode() + "'" + ","+
                        "'" + _tmpdebitor.getUstID() + "'" + "," +
                        VoksTableKunden.SAMMELKONTO + ")";

            try{
                statement.executeUpdate(_query);


            }
            catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                       "INSERT INTO " + VoksTableKunden.KUNDEN_TABLE + " failed :(" + ex
               );
               ApplicationLogger.getInstance().getLogger().severe(
                       "QUERY = " + _query
               );
               return false;

            }


        }

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

        this.close();
        ApplicationLogger.getInstance().getLogger().info("inserting voks debitors without bankdata ... OK");
        return true;
   }


    public boolean updateDebitorWithoutBankdata(List p_voksrecordlist){

        int count;

        count = 0;
        ApplicationLogger.getInstance().getLogger().info("updating voks debitors without bankdata ...");

            this.open();

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


            for (Iterator eone = p_voksrecordlist.iterator(); eone.hasNext();){

                count ++;
                ApplicationLogger.getInstance().getLogger().info("updating " + count + " / " +  p_voksrecordlist.size());
                VoksDebitorRecord _tmpvoksrecord;

                _tmpvoksrecord = (VoksDebitorRecord) eone.next();

                String _query;
                    _query = "UPDATE " +
                           VoksTableAdress.ADDRESS_TABLE +
                           " SET "+
                           VoksTableAdress.NAME                + " = " +
                           "'" + _tmpvoksrecord.getName()         + "'"   +  "," +
                           VoksTableAdress.ZUSATZ              + " = " +
                           "'" + _tmpvoksrecord.getZusatz()       + "'"   +  "," +
                           VoksTableAdress.STRASSE             + " = " +
                           "'" + _tmpvoksrecord.getStrasse()      + "'"   +  "," +
                           VoksTableAdress.LAND                + " = " +
                           "'" + _tmpvoksrecord.getLand()         + "'"   + "," +
                           VoksTableAdress.PLZ                 + " = " +
                           "'" + _tmpvoksrecord.getPLZ()          + "'"   + "," +
                           VoksTableAdress.ORT                 + " = " +
                           "'" + _tmpvoksrecord.getOrt()          + "'"   + "," +
                           VoksTableAdress.TELEFON             + " = " +
                           "'" + _tmpvoksrecord.getTelefon()      + "'"   + "," +
                           VoksTableAdress.FAX                 + " = " +
                           "'" + _tmpvoksrecord.getFax()          + "'"   + "," +
                           VoksTableAdress.EMAIL               + " = " +
                           "'" + _tmpvoksrecord.getEmail()        + "'"   + "," +
                           VoksTableAdress.BANKEINZUG          + " = " +
                           "'" + _tmpvoksrecord.getZahlungsverkehr() + "'" +
                           " WHERE " +
                           VoksTableAdress.KUNDENNUMMER + " = "+
                           "'" + _tmpvoksrecord.getKundenNr() +"'";



                try{
                   statement.executeUpdate(_query);

                }
                catch(SQLException ex){
                   ApplicationLogger.getInstance().getLogger().severe(
                           "UPDATE VFAdress failed :("   + ex
                   );
                    ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = "   + _query
                   );
                   return false;
                }

                _query = "UPDATE  "+
                            VoksTableKunden.KUNDEN_TABLE +
                            " SET " +
                            VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE   + " = " +
                            "'" + _tmpvoksrecord.getZahlungsBedingungsCode() + "'" + ","+
                            VoksTableKunden.USTID  + " = " +
                            "'" + _tmpvoksrecord.getUstID() + "'" +
                            " WHERE " + VoksTableKunden.KUNDENNUMMER + " = " +
                            "'" + _tmpvoksrecord.getKundenNr() + "'";

                try{
                    statement.executeUpdate(_query);


                }
                catch(SQLException ex){
                    ApplicationLogger.getInstance().getLogger().severe(
                           "INSERT INTO " + VoksTableKunden.KUNDEN_TABLE + " failed :(" + ex
                   );
                   ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = " + _query
                   );
                   return false;

                }


            }

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

            this.close();
        ApplicationLogger.getInstance().getLogger().info("updating voks debitors without bankdata ... OK");
            return true;
       }

    public List getAllDebitors() throws SQLException {

        // local variables
        List _voksrecordlist;
        ResultSet _rs;
        String _query;

        //init
        _voksrecordlist = new LinkedList();
        _query = "SELECT" +
                " A." + VoksTableAdress.KUNDENNUMMER       + "," +
                " A." + VoksTableAdress.NAME               + "," +
                " A." + VoksTableAdress.ZUSATZ             + "," +
                " A." + VoksTableAdress.STRASSE            + "," +
                " A." + VoksTableAdress.LAND               + "," +
                " A." + VoksTableAdress.PLZ                + "," +
                " A." + VoksTableAdress.ORT                + "," +
                " A." + VoksTableAdress.TELEFON            + "," +
                " A." + VoksTableAdress.FAX                + "," +
                " A." + VoksTableAdress.EMAIL              + "," +
                " A." + VoksTableAdress.BANKKONTONUMMER    + "," +
                " A." + VoksTableAdress.BANKLEITZAHL       + "," +
                " A." + VoksTableAdress.BANKNAME           + "," +
                " A." + VoksTableAdress.BANKEINZUG         + "," +
                " K." + VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE + "," +
                " K." + VoksTableKunden.USTID                  +
                " FROM " +
                VoksTableAdress.ADDRESS_TABLE + " A "+  "," +
                VoksTableKunden.KUNDEN_TABLE + " K "+
                " WHERE" +
                " A." + VoksTableAdress.KUNDENNUMMER +
                " = " +
                " K." + VoksTableKunden.KUNDENNUMMER;

        this.open();

        try{
			_rs = statement.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the VoksDB failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            VoksDebitorRecord _tmprecord;
            _tmprecord = new VoksDebitorRecord();

            _tmprecord.setBankKontoNr(_rs.getString(VoksTableAdress.BANKKONTONUMMER));
            _tmprecord.setBankLeitZahl(_rs.getString(VoksTableAdress.BANKLEITZAHL));
            _tmprecord.setBankName(_rs.getString(VoksTableAdress.BANKNAME));
            _tmprecord.setEmail(_rs.getString(VoksTableAdress.EMAIL));
            _tmprecord.setFaxNummer(_rs.getString(VoksTableAdress.FAX));
            _tmprecord.setKundenNr(_rs.getString(VoksTableAdress.KUNDENNUMMER));
            _tmprecord.setLand(_rs.getString(VoksTableAdress.LAND));
            _tmprecord.setName(_rs.getString(VoksTableAdress.NAME));
            _tmprecord.setOrt(_rs.getString(VoksTableAdress.ORT));
            _tmprecord.setPlz(_rs.getString(VoksTableAdress.PLZ));
            _tmprecord.setStrasse(_rs.getString(VoksTableAdress.STRASSE));
            _tmprecord.setTelefon((_rs.getString(VoksTableAdress.TELEFON)));
            _tmprecord.setUstID(_rs.getString(VoksTableKunden.USTID));
            _tmprecord.setZahlungsBedingungsCode(_rs.getString(VoksTableKunden.ZAHLUNGSBEDINGUNGSCODE));
            _tmprecord.setZahlungsverkehr(_rs.getString(VoksTableAdress.BANKEINZUG));
            _tmprecord.setZusatz(_rs.getString(VoksTableAdress.ZUSATZ));

            _voksrecordlist.add(_tmprecord);

        }


        this.close();

        return _voksrecordlist;
    }

    public List getAllKreditors() throws SQLException {

        // local variables
        List _voksrecordlist;
        ResultSet _rs;
        String _query;

        //init
        _voksrecordlist = new LinkedList();
        _query = "SELECT" +
                " A." + VoksTableAdress.LIEFERANTENNUMMER       + "," +
                " A." + VoksTableAdress.NAME               + "," +
                " A." + VoksTableAdress.ZUSATZ             + "," +
                " A." + VoksTableAdress.STRASSE            + "," +
                " A." + VoksTableAdress.LAND               + "," +
                " A." + VoksTableAdress.PLZ                + "," +
                " A." + VoksTableAdress.ORT                + "," +
                " A." + VoksTableAdress.TELEFON            + "," +
                " A." + VoksTableAdress.FAX                + "," +
                " A." + VoksTableAdress.EMAIL              + "," +
                " A." + VoksTableAdress.BANKKONTONUMMER    + "," +
                " A." + VoksTableAdress.BANKLEITZAHL       + "," +
                " A." + VoksTableAdress.BANKNAME           + "," +
                " A." + VoksTableAdress.BANKEINZUG         + "," +
                " L." + VoksTableLieferanten.ZAHLUNGSBEDINGUNGSCODE + "," +
                " L." + VoksTableLieferanten.EIGENEKUNDENNUMMER +
                " FROM " +
                VoksTableAdress.ADDRESS_TABLE + " A " + "," +
                VoksTableLieferanten.LIEFERANTEN_TABLE + " L "+
                " WHERE" +
                " A." + VoksTableAdress.LIEFERANTENNUMMER +
                " = " +
                " L." + VoksTableLieferanten.LIEFERANTENNUMMER;

        this.open();

        try{
			_rs = statement.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the VoksDB failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            VoksKreditorRecord _tmprecord;
            _tmprecord = new VoksKreditorRecord();

            _tmprecord.setBankKontoNr(_rs.getString(VoksTableAdress.BANKKONTONUMMER));
            _tmprecord.setBankLeitZahl(_rs.getString(VoksTableAdress.BANKLEITZAHL));
            _tmprecord.setBankName(_rs.getString(VoksTableAdress.BANKNAME));
            _tmprecord.setEmail(_rs.getString(VoksTableAdress.EMAIL));
            _tmprecord.setFaxNummer(_rs.getString(VoksTableAdress.FAX));
            _tmprecord.setLieferantenNr(_rs.getString(VoksTableAdress.LIEFERANTENNUMMER));
            _tmprecord.setLand(_rs.getString(VoksTableAdress.LAND));
            _tmprecord.setName(_rs.getString(VoksTableAdress.NAME));
            _tmprecord.setOrt(_rs.getString(VoksTableAdress.ORT));
            _tmprecord.setPlz(_rs.getString(VoksTableAdress.PLZ));
            _tmprecord.setStrasse(_rs.getString(VoksTableAdress.STRASSE));
            _tmprecord.setTelefon((_rs.getString(VoksTableAdress.TELEFON)));
            _tmprecord.setEigeneKundenNr(_rs.getString(VoksTableLieferanten.EIGENEKUNDENNUMMER));
            _tmprecord.setZahlungsBedingungsCode(_rs.getString(VoksTableLieferanten.ZAHLUNGSBEDINGUNGSCODE));
            _tmprecord.setZahlungsverkehr(_rs.getString(VoksTableAdress.BANKEINZUG));
            _tmprecord.setZusatz(_rs.getString(VoksTableAdress.ZUSATZ));

            _voksrecordlist.add(_tmprecord);

        }


        this.close();

        return _voksrecordlist;
    }

    public boolean updateKreditorWithoutBankdata(List p_voksrecordlist){

        int count;

        count = 0;
        ApplicationLogger.getInstance().getLogger().info("updating voks kreditors without bankdata ...");

            this.open();

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


            for (Iterator eone = p_voksrecordlist.iterator(); eone.hasNext();){

                count ++;
                ApplicationLogger.getInstance().getLogger().info("updating " + count + " / " +  p_voksrecordlist.size());
                VoksKreditorRecord _tmpvoksrecord;

                _tmpvoksrecord = (VoksKreditorRecord) eone.next();

                String _query;
                    _query = "UPDATE " +
                           VoksTableAdress.ADDRESS_TABLE +
                           " SET "+
                           VoksTableAdress.NAME                + " = " +
                           "'" + _tmpvoksrecord.getName()         + "'"   +  "," +
                           VoksTableAdress.ZUSATZ              + " = " +
                           "'" + _tmpvoksrecord.getZusatz()       + "'"   +  "," +
                           VoksTableAdress.STRASSE             + " = " +
                           "'" + _tmpvoksrecord.getStrasse()      + "'"   +  "," +
                           VoksTableAdress.LAND                + " = " +
                           "'" + _tmpvoksrecord.getLand()         + "'"   + "," +
                           VoksTableAdress.PLZ                 + " = " +
                           "'" + _tmpvoksrecord.getPLZ()          + "'"   + "," +
                           VoksTableAdress.ORT                 + " = " +
                           "'" + _tmpvoksrecord.getOrt()          + "'"   + "," +
                           VoksTableAdress.TELEFON             + " = " +
                           "'" + _tmpvoksrecord.getTelefon()      + "'"   + "," +
                           VoksTableAdress.FAX                 + " = " +
                           "'" + _tmpvoksrecord.getFax()          + "'"   + "," +
                           VoksTableAdress.EMAIL               + " = " +
                           "'" + _tmpvoksrecord.getEmail()        + "'"   + "," +
                           VoksTableAdress.BANKEINZUG          + " = " +
                           "'" + _tmpvoksrecord.getZahlungsverkehr() + "'" +
                           " WHERE " +
                           VoksTableAdress.LIEFERANTENNUMMER + " = "+
                           "'" + _tmpvoksrecord.getLieferantenNr() +"'";



                try{
                   statement.executeUpdate(_query);

                }
                catch(SQLException ex){
                   ApplicationLogger.getInstance().getLogger().severe(
                           "UPDATE VFAdress failed :("   + ex
                   );
                    ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = "   + _query
                   );
                   return false;
                }

                _query = "UPDATE  "+
                            VoksTableLieferanten.LIEFERANTEN_TABLE +
                            " SET " +
                            VoksTableLieferanten.ZAHLUNGSBEDINGUNGSCODE   + " = " +
                            "'" + _tmpvoksrecord.getZahlungsBedingungsCode() + "'" + ","+
                            VoksTableLieferanten.EIGENEKUNDENNUMMER  + " = " +
                            "'" + _tmpvoksrecord.getEigeneKundenNr() + "'" +
                            " WHERE " + VoksTableLieferanten.LIEFERANTENNUMMER + " = " +
                            "'" + _tmpvoksrecord.getLieferantenNr() + "'";

                try{
                    statement.executeUpdate(_query);


                }
                catch(SQLException ex){
                    ApplicationLogger.getInstance().getLogger().severe(
                           "INSERT INTO " + VoksTableLieferanten.LIEFERANTEN_TABLE + " failed :(" + ex
                   );
                   ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = " + _query
                   );
                   return false;

                }


            }

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

            this.close();
        ApplicationLogger.getInstance().getLogger().info("updating voks kreditors without bankdata ... OK");
            return true;
       }

    public boolean insertKreditorWithoutBankdata(List p_voksrecords){

            int count = 0;

            ApplicationLogger.getInstance().getLogger().info("inserting voks kreditors without bankdata ...");

            this.open();

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


            for (Iterator eone = p_voksrecords.iterator(); eone.hasNext();){

                count ++;

                ApplicationLogger.getInstance().getLogger().info("inserting voks kreditors " + count + " / " + p_voksrecords.size());

                VoksKreditorRecord _tmprecord;
                int _id;

                _tmprecord = (VoksKreditorRecord) eone.next();

                String _query;
                    _query = "INSERT INTO " +
                           VoksTableAdress.ADDRESS_TABLE +
                           " ("+
                           VoksTableAdress.LIEFERANTENNUMMER       + "," +
                           VoksTableAdress.NAME               + "," +
                           VoksTableAdress.ZUSATZ             + "," +
                           VoksTableAdress.STRASSE            + "," +
                           VoksTableAdress.LAND               + "," +
                           VoksTableAdress.PLZ                + "," +
                           VoksTableAdress.ORT                + "," +
                           VoksTableAdress.TELEFON            + "," +
                           VoksTableAdress.FAX                + "," +
                           VoksTableAdress.EMAIL              + "," +
                           VoksTableAdress.BANKEINZUG         +
                           ") VALUES (" +
                           _tmprecord.getLieferantenNr()                     + "," +
                           "'" + _tmprecord.getName()   + "'"           + "," +
                           "'" + _tmprecord.getZusatz() + "'"           + "," +
                           "'" + _tmprecord.getStrasse() + "'"          + "," +
                           "'" + _tmprecord.getLand() + "'"             + "," +
                           "'" + _tmprecord.getPLZ() + "'"              + "," +
                           "'" + _tmprecord.getOrt() + "'"              + "," +
                           "'" + _tmprecord.getTelefon() + "'"          + "," +
                           "'" + _tmprecord.getFax() + "'"              + "," +
                           "'" + _tmprecord.getEmail() + "'"            + "," +
                           "'" + _tmprecord.getZahlungsverkehr() + "'"  + ")";

                try{
                   statement.executeUpdate(_query);

                }
                catch(SQLException ex){
                   ApplicationLogger.getInstance().getLogger().severe(
                           "UPDATE VFAdress failed :("   + ex
                   );
                    ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = "   + _query
                   );
                   return false;
                }

                _query = "SELECT ID FROM "+
                            VoksTableAdress.ADDRESS_TABLE +
                            " WHERE "+
                            VoksTableAdress.LIEFERANTENNUMMER +
                            " = " +
                            "'" + _tmprecord.getLieferantenNr() + "'";

                try{
                    ResultSet _rs;

                    _rs = statement.executeQuery(_query);
                     _rs.next();
                    _id =  _rs.getInt("ID");

                }
                catch(SQLException ex){
                    ApplicationLogger.getInstance().getLogger().severe(
                           "SELECT ID FROM " + VoksTableLieferanten.LIEFERANTEN_TABLE+ " failed :(" + ex
                   );
                    ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = " + _query
                   );
                   return false;

                }

                _query = "INSERT INTO "+
                            VoksTableLieferanten.LIEFERANTEN_TABLE +
                            " (" +
                            VoksTableLieferanten.LIEFERANTENNUMMER   + "," +
                            VoksTableLieferanten.ADRESSID   + "," +
                            VoksTableLieferanten.ZAHLUNGSBEDINGUNGSCODE   + "," +
                            VoksTableLieferanten.EIGENEKUNDENNUMMER  + "," +
                            VoksTableLieferanten.SAMMELKONTOKREDITOR   + ")" +
                            " VALUES (" +
                            _tmprecord.getLieferantenNr() + "," +
                            _id + "," +
                            "'" + _tmprecord.getZahlungsBedingungsCode() + "'" + ","+
                            "'" + _tmprecord.getEigeneKundenNr() + "'" + "," +
                            VoksTableLieferanten.SAMMELKONTO + ")";

                try{
                    statement.executeUpdate(_query);


                }
                catch(SQLException ex){
                    ApplicationLogger.getInstance().getLogger().severe(
                           "INSERT INTO " + VoksTableLieferanten.LIEFERANTEN_TABLE + " failed :(" + ex
                   );
                   ApplicationLogger.getInstance().getLogger().severe(
                           "QUERY = " + _query
                   );
                   return false;

                }


            }

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

            this.close();
            ApplicationLogger.getInstance().getLogger().info("inserting voks kreditors without bankdata ... OK");
            return true;
       }


};

