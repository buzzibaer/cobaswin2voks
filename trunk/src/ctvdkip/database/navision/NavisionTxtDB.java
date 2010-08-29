package ctvdkip.database.navision;

import ctvdkip.util.ApplicationLogger;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;
import ctvdkip.database.voks.VoksRecord;
import ctvdkip.database.voks.AccountingRecord;


import java.sql.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



/**
 *
 * navision Database Acces Class
 * 
 * @author rbust
 */
public class NavisionTxtDB {
	
	/**
	 * The database connection object Debitor1
	 */
	private Connection dbConnection;

	
	/**
	 * dbConnectionstructor for navision DB
	 *
	 */
	public NavisionTxtDB(){
	};
	
	/**
	 * 
	 * method is checking the given payment codes against the NavisionExelDB.
	 * import should be canceled if not equal
	 *
	 *
	 */
	public List getAllPaymentCodes(String p_table) throws SQLException{

        // local variables
		String _query;
		ResultSet _rs;
		Statement _stmt;
		List _zahlbedausNavision;

        // init
        _zahlbedausNavision = new LinkedList();
        _query = "SELECT DISTINCT "+
                NavisionDebitorConstants.ZAHLUNGSBEDINGUNGSCODE +
                " FROM "+
                p_table;

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
		
		try{
			_rs = _stmt.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the navision DB failed :(" + ex
			);
            ApplicationLogger.getInstance().getLogger().severe(
					"Query = " +_query
			);
			throw ex;
		}

        while (_rs.next()) {

            Integer _int;
		    _int = new Integer(_rs.getInt(NavisionDebitorConstants.ZAHLUNGSBEDINGUNGSCODE));
            _zahlbedausNavision.add(_int);
        }

/*
		try{
			while (_rs.next()){

				Integer _int;
				_int = new Integer(_rs.getInt("ZB"));

				ApplicationLogger.getInstance().getLogger().info(
						"ZB Value = "+ _int
				);


				boolean found = false;

				for (Iterator enum = _zahlbedausNavision.iterator() ; enum.hasNext() ; ){
					if (_int.compareTo((Integer) enum.next()) == 0){
						//already exists leaving it untouched

						ApplicationLogger.getInstance().getLogger().info(
								"ZB Value = "+
								_int +
								" already exists, leavin it untouched"
						);
/
						found = true;
						break;
					}
				}
				if (!found){
					_zahlbedausNavision.add(_int);

					ApplicationLogger.getInstance().getLogger().info(
							"ZB Value = "+
							_int +
							" is NEW, will be added"
					);

				}
				found = false;

			};
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not get ResultSet from navision DB :( = "+ ex
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
					"Could not close ResultSet from navision DB :( = "+ ex
			);
		}

		try{
			_stmt.close();
			ApplicationLogger.getInstance().getLogger().info(
					"Closing Statement = OK"
			);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close Statement from navision DB :( = "+ ex
			);
		}

*/
		try{
			_rs.close();
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close ResultSet from navision DB :( = "+ ex
			);
		}

		try{
			_stmt.close();
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close Statement from navision DB :( = "+ ex
			);
		}
		this.close();

		ApplicationLogger.getInstance().getLogger().info(
				"Elements of List" + _zahlbedausNavision
		);

		return _zahlbedausNavision;

	};//end of method checkForPaymentCodes()

	/**
	 *
	 * opens the dbConnectionnectino to the database
	 *
	 * @return boolean = false if dbConnectionnection could not be opend
	 */
	private boolean open(){
		
		// odbc string
		String _odbcstring = "sun.jdbc.odbc.JdbcOdbcDriver";
		String _dbConnectionString = "jdbc:odbc:navision";

		// Loading Driver and opening dbConnectionnection
		try{
			Class.forName(_odbcstring);
		}
		catch(ClassNotFoundException _classNotFoundEx){
			ApplicationLogger.getInstance().getLogger().severe(
					"ODBC String for  DB is not valid :("
			);
			return false;
		}
		
		try{
			dbConnection = DriverManager.getConnection(_dbConnectionString);

		}
		catch (SQLException p_sqlex ) {
			
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not open DB. DB is configured for exclusivly and maybe still open by ?"
			);
			return false;
		}
		
		ApplicationLogger.getInstance().getLogger().info(
				"Opening NavisionTxtDB = OK"
		);	
		
		return true;
	};
	
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
					"Could not close DB. Maybe it is already closed?"
			);
			return false;
		};				
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
     * Method is returning a List of VoksDebitorRecords
     * @return
     * @throws SQLException
     */
	public List getAllDebitors() throws SQLException{

        // *******************************************************
        // local variavles
		List r_list;
        String _query;
		ResultSet _rs;
		Statement _stmt;
        // *******************************************************

        // *******************************************************
        // init
        r_list = new LinkedList();
        _query = "SELECT"+
                " deb1."+ NavisionDebitorConstants.KUNDENNUMMER + "," +
                " deb1."+ NavisionDebitorConstants.NAME +         "," +
                " deb1."+ NavisionDebitorConstants.ZUSATZ +       "," +
                " deb1."+ NavisionDebitorConstants.STRASSE +      "," +
                " deb1."+ NavisionDebitorConstants.PLZUNDORT +    "," +
                " deb1."+ NavisionDebitorConstants.LAND +         "," +
                " deb1."+ NavisionDebitorConstants.TELEFON +      "," +
                " deb1."+ NavisionDebitorConstants.FAX +          "," +
                " deb2."+ NavisionDebitorConstants.USTID +        "," +
                " deb2."+ NavisionDebitorConstants.ZAHLUNGSBEDINGUNGSCODE + "," +
                " deb2."+ NavisionDebitorConstants.BANKEINZUG +             "," +
                " deb2."+ NavisionDebitorConstants.BANKNAME +               "," +
                " deb2."+ NavisionDebitorConstants.BANKLEITZAHL +           "," +
                " deb2."+ NavisionDebitorConstants.SALDO +           "," +
                " deb2."+ NavisionDebitorConstants.BANKKONTONUMMER +
                " FROM "+
                NavisionDebitorConstants.DEBITORFILE1 +
                " deb1, "+
                NavisionDebitorConstants.DEBITORFILE2 +
                " deb2 "+
                " WHERE"+
                " deb1."+ NavisionDebitorConstants.KUNDENNUMMER +
                " = "+
                "deb2."+ NavisionDebitorConstants.KUNDENNUMMER;
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

            VoksDebitorRecord _debitorfromnavision;
            _debitorfromnavision = new VoksDebitorRecord();

            _debitorfromnavision.setKundenNr(_rs.getInt(NavisionDebitorConstants.KUNDENNUMMER));
            _debitorfromnavision.setName(_rs.getString(NavisionDebitorConstants.NAME));
            _debitorfromnavision.setZusatz(_rs.getString(NavisionDebitorConstants.ZUSATZ));
            _debitorfromnavision.setStrasse(_rs.getString(NavisionDebitorConstants.STRASSE));
            _debitorfromnavision.setFaxNummer(_rs.getString(NavisionDebitorConstants.FAX));
            _debitorfromnavision.setLand(_rs.getString(NavisionDebitorConstants.LAND));
            _debitorfromnavision.setTelefon(_rs.getString(NavisionDebitorConstants.TELEFON));
            _debitorfromnavision.setUstID(_rs.getString(NavisionDebitorConstants.USTID));
            _debitorfromnavision.setZahlungsBedingungsCode(_rs.getString(NavisionDebitorConstants.ZAHLUNGSBEDINGUNGSCODE));
            _debitorfromnavision.setBankName(_rs.getString(NavisionDebitorConstants.BANKNAME));
            _debitorfromnavision.setBankKontoNr(_rs.getString(NavisionDebitorConstants.BANKKONTONUMMER));
            _debitorfromnavision.setSaldo(_rs.getString(NavisionDebitorConstants.SALDO));
            _debitorfromnavision.setBankLeitZahl(_rs.getString(NavisionDebitorConstants.BANKLEITZAHL));


            // PLZ und ORT zerlegen
            String _plzundort = null;
            _plzundort = _rs.getString(NavisionDebitorConstants.PLZUNDORT);

            //Pattern _pattern = Pattern.compile("\\d\\d\\d\\d\\d");
            if (_plzundort == null){
                ApplicationLogger.getInstance().getLogger().severe(
				    "NULL DETECTED in Kundennr = " +_debitorfromnavision.getKundenNr()
		        );
                ApplicationLogger.getInstance().getLogger().severe(
				    "DELETE \" FROM KUNDE"
		        );
            }
            int _int = _plzundort.indexOf(' ');
            if (_int == 5){
                 // Deutesche Postleitzahl gefunden

                String[] _splittedstring;
                _splittedstring = Pattern.compile(" ").split(_plzundort,2);

                _debitorfromnavision.setPlz(_splittedstring[0]);
                _debitorfromnavision.setOrt(_splittedstring[1]);
            }
            else{
                ApplicationLogger.getInstance().getLogger().info(
				    "Ausländische Postleitzahl gefunden = " + _plzundort  +" KundenNr = "+ _debitorfromnavision.getKundenNr()
		        );

                Pattern _pattern = Pattern.compile("\\d{3,}\\s{1,}");
                Matcher _matcher = _pattern.matcher(_plzundort);
                boolean match = _matcher.lookingAt();
                match = _matcher.lookingAt();

                if(match){
                    ApplicationLogger.getInstance().getLogger().info(
				        "Mindestens 3 Zahlen und 1 Whitespace gefunden "
		            );
                    String[] _splittedstring;
                    _splittedstring = Pattern.compile(" ").split(_plzundort,2);

                    _debitorfromnavision.setPlz(_splittedstring[0]);
                    _debitorfromnavision.setOrt(_splittedstring[1]);
                }
                else{

                    Pattern _testpattern = Pattern.compile("^\\d{5,5}\\s{0,0}");
                    Matcher _testmatcher = _testpattern.matcher(_plzundort);
                    boolean matched = _testmatcher.lookingAt();

                    if(matched){
                        ApplicationLogger.getInstance().getLogger().info(
				            "Falsch formatierter deutscher Kunde gefunden. Fixing it ..."
                        );
                        _debitorfromnavision.setPlz(_plzundort.substring(1,5));
                        _debitorfromnavision.setOrt(_plzundort.substring(5));
                    }
                    else{
                        ApplicationLogger.getInstance().getLogger().info(
				            "Keine 3 Zahlen gefunden :( Schiebe alles in Ort"
                        );
                        _debitorfromnavision.setPlz("");
                        _debitorfromnavision.setOrt(_plzundort);
                    }

                }


            }

            String _tmpstring;
            _tmpstring = _rs.getString(NavisionDebitorConstants.BANKEINZUG);
            if(_tmpstring == null){
                _tmpstring = "";
            }
            _tmpstring =_tmpstring.trim();

            if (_tmpstring.equalsIgnoreCase("Ja")){
                _debitorfromnavision.setZahlungsverkehr("L");
            }
            else{
                _debitorfromnavision.setZahlungsverkehr("N");

            }
            r_list.add(_debitorfromnavision);
        }



		ApplicationLogger.getInstance().getLogger().info(
				"Quering navision DB for AllData = OK"
		);

        this.close();

		return r_list;
	}


	
	/**
	 * method is checking consistens of Debitor1 and Debitor2 File.
	 * Counting cusumor ids and checking which are corrupt.
	 * 
	 * @return
	 */
	private boolean checkConsistentFiles(String p_file1, String p_file2) throws SQLException{
		
		//*****************************************************
		// local variables
		//*****************************************************
		List _fromFile1;
        List _fromFile2;
        String _query1 = "SELECT DISTINCT NUMMER FROM " + p_file1;
		String _query2 = "SELECT DISTINCT NUMMER FROM " + p_file2;
        ResultSet _rs;
		Statement _stmt;
		//*****************************************************

		_fromFile2 = new LinkedList();
        _fromFile1 = new LinkedList();

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
		
		try{
			_rs = _stmt.executeQuery(_query1);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the navision DB with _stmt failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            Integer _kundennummer;

            _kundennummer = new Integer (_rs.getInt("NUMMER"));

            _fromFile1.add(_kundennummer);
        }



		try{
			_rs = _stmt.executeQuery(_query2);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the navision DB with _stmt failed :(" + ex
			);
			throw ex;
		}

        while (_rs.next()){

            Integer _kundennummer;

            _kundennummer = new Integer (_rs.getInt("NUMMER"));

            _fromFile2.add(_kundennummer);
        }


		
		
		if(!this.getOverlapElements(_fromFile1,_fromFile2).isEmpty()){

			ApplicationLogger.getInstance().getLogger().severe(
				"Found Overlap Elements in Files"
			);
			return false;
		}
		
		_stmt.close();
		this.close();
		
		
		return true;
	}//end of method checkConsistenDebitorFiles()


	private List getOverlapElements(List _one, List _two){
		
		List r_list= new LinkedList();
		boolean _found = false;
		
		if (_one.size() > _two.size()){
			
			ApplicationLogger.getInstance().getLogger().info(
					"Vector _one > _two"
			);
			
			for (Iterator eone = _one.iterator();eone.hasNext();){
				
				Integer _IntegerOne = (Integer) eone.next();
				
				for (Iterator etwo = _two.iterator(); etwo.hasNext();){
					
					Integer _IntegerTwo = (Integer) etwo.next();
					
					if(_IntegerOne.compareTo(_IntegerTwo) == 0){
						
						_found = true;
						break;
					}
				}
				
				if (true == _found){
/*					
					ApplicationLogger.getInstance().getLogger().info(
							"Element found in Vector"
					);
*/					
					_found = false;
				}else{
					ApplicationLogger.getInstance().getLogger().info(
							"Element NOT found in List adding to retrun List = " + _IntegerOne
					);
					
					r_list.add(_IntegerOne);
				}
			}
		}
		else if (_one.size() < _two.size()){
			
			ApplicationLogger.getInstance().getLogger().info(
					"Vector _two > _one"
			);
			
			for (Iterator etwo = _two.iterator();etwo.hasNext();){
				
				Integer _IntegerFromListTwo = (Integer) etwo.next();
				
				for (Iterator eone = _one.iterator(); eone.hasNext();){
					
					Integer _IntegerFromListOne = (Integer) eone.next();
					
					if(_IntegerFromListOne.compareTo(_IntegerFromListTwo) == 0){
						
						_found = true;
						break;
					}
				}
				
				if (true == _found){
/*
					ApplicationLogger.getInstance().getLogger().info(
							"Element found in Vector"
					);
*/
					_found = false;
				}else{
					ApplicationLogger.getInstance().getLogger().info(
							"Element NOT found in List adding to retrun Vector" + _IntegerFromListTwo
					);
					
					r_list.add(_IntegerFromListTwo);
				}
			}
		}else{
			ApplicationLogger.getInstance().getLogger().info(
					"Size of Vector are equal. Return Vector will be size = 0"
			);
		}

        if (!r_list.isEmpty()){
            ApplicationLogger.getInstance().getLogger().severe(
                    "Overlap Elements of Vector" + r_list
            );
        }
		return r_list;
	}

    	public List getAllKreditors() throws SQLException{

        // *******************************************************
        // local variavles
		List r_list;
        String _query;
		ResultSet _rs;
		Statement _stmt;
        // *******************************************************

        // *******************************************************
        // init
        r_list = new LinkedList();
        _query = "SELECT"+
                " kred1."+ NavisionKreditorConstants.LIEFERANTENNUMMER + "," +
                " kred1."+ NavisionKreditorConstants.NAME +         "," +
                " kred1."+ NavisionKreditorConstants.ZUSATZ +       "," +
                " kred1."+ NavisionKreditorConstants.STRASSE +      "," +
                " kred1."+ NavisionKreditorConstants.PLZUNDORT +    "," +
                " kred1."+ NavisionKreditorConstants.LAND +         "," +
                " kred1."+ NavisionKreditorConstants.TELEFON +      "," +
                " kred1."+ NavisionKreditorConstants.FAX +          "," +
                " kred1."+ NavisionKreditorConstants.EIGENEKUNDENNUMMER +        "," +
                " kred1."+ NavisionKreditorConstants.ZAHLUNGSBEDINGUNGSCODE + "," +
                " kred2."+ NavisionKreditorConstants.BANKNAME +               "," +
                " kred2."+ NavisionKreditorConstants.BANKLEITZAHL +           "," +
                " kred2."+ NavisionKreditorConstants.SALDO +           "," +
                " kred2."+ NavisionKreditorConstants.BANKKONTONUMMER +
                " FROM "+
                NavisionKreditorConstants.KREDITORFILE1 +
                " kred1, "+
                NavisionKreditorConstants.KREDITORFILE2 +
                " kred2 "+
                " WHERE"+
                " kred1."+ NavisionKreditorConstants.LIEFERANTENNUMMER +
                " = "+
                "kred2."+ NavisionKreditorConstants.LIEFERANTENNUMMER;
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

            VoksKreditorRecord _kreditorfromnavision;
            _kreditorfromnavision = new VoksKreditorRecord();

            _kreditorfromnavision.setLieferantenNr(_rs.getInt(NavisionKreditorConstants.LIEFERANTENNUMMER));
            _kreditorfromnavision.setName(_rs.getString(NavisionKreditorConstants.NAME));
            _kreditorfromnavision.setZusatz(_rs.getString(NavisionKreditorConstants.ZUSATZ));
            _kreditorfromnavision.setStrasse(_rs.getString(NavisionKreditorConstants.STRASSE));
            _kreditorfromnavision.setFaxNummer(_rs.getString(NavisionKreditorConstants.FAX));
            _kreditorfromnavision.setLand(_rs.getString(NavisionKreditorConstants.LAND));
            _kreditorfromnavision.setTelefon(_rs.getString(NavisionKreditorConstants.TELEFON));
            _kreditorfromnavision.setEigeneKundenNr(_rs.getString(NavisionKreditorConstants.EIGENEKUNDENNUMMER));
            _kreditorfromnavision.setZahlungsBedingungsCode(_rs.getString(NavisionKreditorConstants.ZAHLUNGSBEDINGUNGSCODE));
            _kreditorfromnavision.setBankName(_rs.getString(NavisionKreditorConstants.BANKNAME));
            _kreditorfromnavision.setBankKontoNr(_rs.getString(NavisionKreditorConstants.BANKKONTONUMMER));
            _kreditorfromnavision.setSaldo(_rs.getString(NavisionKreditorConstants.SALDO));            
            _kreditorfromnavision.setBankLeitZahl(_rs.getString(NavisionKreditorConstants.BANKLEITZAHL));


            // PLZ und ORT zerlegen
            String _plzundort = null;
            _plzundort = _rs.getString(NavisionKreditorConstants.PLZUNDORT);


            if (_plzundort == null){
                ApplicationLogger.getInstance().getLogger().severe(
				    "NULL DETECTED in LIEFERANTENNUMMER = " +_kreditorfromnavision.getLieferantenNr());
                ApplicationLogger.getInstance().getLogger().severe(
				    "DELETE \" FROM LIEFERANT"
		        );
            }
            int _int = _plzundort.indexOf(' ');
            if (_int == 5){
                 // Deutesche Postleitzahl gefunden

                String[] _splittedstring;
                _splittedstring = Pattern.compile(" ").split(_plzundort,2);

                _kreditorfromnavision.setPlz(_splittedstring[0]);
                _kreditorfromnavision.setOrt(_splittedstring[1]);
            }
            else{
                ApplicationLogger.getInstance().getLogger().info(
				    "Ausländische Postleitzahl gefunden = " + _plzundort  +" LieferantenNr = "+ _kreditorfromnavision.getLieferantenNr());

                Pattern _pattern = Pattern.compile("\\d{3,}\\s{1,}");
                Matcher _matcher = _pattern.matcher(_plzundort);
                boolean match = _matcher.lookingAt();
                match = _matcher.lookingAt();

                if(match){
                    ApplicationLogger.getInstance().getLogger().info(
				        "Mindestens 3 Zahlen und 1 Whitespace gefunden "
		            );
                    String[] _splittedstring;
                    _splittedstring = Pattern.compile(" ").split(_plzundort,2);

                    _kreditorfromnavision.setPlz(_splittedstring[0]);
                    _kreditorfromnavision.setOrt(_splittedstring[1]);
                }
                else{

                    Pattern _testpattern = Pattern.compile("^\\d{5,5}\\s{0,0}");
                    Matcher _testmatcher = _testpattern.matcher(_plzundort);
                    boolean matched = _testmatcher.lookingAt();

                    if(matched){
                        ApplicationLogger.getInstance().getLogger().info(
				            "Falsch formatierter deutscher Kunde gefunden. Fixing it ..."
                        );
                        _kreditorfromnavision.setPlz(_plzundort.substring(1,5));
                        _kreditorfromnavision.setOrt(_plzundort.substring(5));
                    }
                    else{
                        ApplicationLogger.getInstance().getLogger().info(
				            "Keine 3 Zahlen gefunden :( Schiebe alles in Ort"
                        );
                        _kreditorfromnavision.setPlz("");
                        _kreditorfromnavision.setOrt(_plzundort);
                    }

                }


            }

            String _tmpstring;
            _tmpstring = _kreditorfromnavision.getBankKontoNr();
            if (_tmpstring == null){
                _tmpstring = "";
            }
            _tmpstring =_tmpstring.trim();

            if (_tmpstring.equalsIgnoreCase("")){
                _kreditorfromnavision.setZahlungsverkehr("N");
            }
            else{
                _kreditorfromnavision.setZahlungsverkehr("B");

            }
            r_list.add(_kreditorfromnavision);
        }



		ApplicationLogger.getInstance().getLogger().info(
				"Quering navision DB for AllData = OK"
		);

        this.close();

		return r_list;
	}

    public boolean checkConsistentFiles(){
        //checking consistenz of debitore files for TXT File Version
        try {

            if (!this.checkConsistentFiles(NavisionDebitorConstants.DEBITORFILE1, NavisionDebitorConstants.DEBITORFILE2)) {
                ApplicationLogger.getInstance().getLogger().severe("Debitor files NOT consistent :(");
                return false;
            }
            ;
        } catch (SQLException ex) {
            ApplicationLogger.getInstance().getLogger().severe("Error while checking consistent DEBITOR files :(" + ex);
            return false;
        }

        //checking consistenz of kreditor files for TXT File Version
        try {

            if (!this.checkConsistentFiles(NavisionKreditorConstants.KREDITORFILE1, NavisionKreditorConstants.KREDITORFILE2)) {
                ApplicationLogger.getInstance().getLogger().severe("Kreditor files NOT consistent :(");
                return false;
            }
            ;
        } catch (SQLException ex) {
            ApplicationLogger.getInstance().getLogger().severe("Error while checking consistent KREDITOR files :(" + ex);
            return false;
        }
        return true;

    }

    public List getAllInitizalDebitorOPAccountingRecords() throws SQLException{

        // *******************************************************
        // local variavles
		List r_list;
        String _query;
		ResultSet _rs;
		Statement _stmt;
        // *******************************************************

        // *******************************************************
        // init
        r_list = new LinkedList();
        _query = "SELECT "+
                NavisionDebitorOPConstants.BELEGDATUM +         "," +
                NavisionDebitorOPConstants.BELEGNUMMER +         "," +
                NavisionDebitorOPConstants.BELEGTEXT +         "," +
                NavisionDebitorOPConstants.KONTO + "," +
                NavisionDebitorOPConstants.GEGENKONTO +         "," +
                NavisionDebitorOPConstants.BETRAG +
                " FROM "+
                NavisionDebitorOPConstants.OP_DEBITORFILE;
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

		try{
			_rs = _stmt.executeQuery(_query);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query File "+NavisionDebitorOPConstants.OP_DEBITORFILE+" failed :(" + ex
			);
            ApplicationLogger.getInstance().getLogger().severe(
                    "Query = " +_query
            );
			throw ex;
		}

        while (_rs.next()){

            AccountingRecord _record;
            _record = new AccountingRecord();

            _record.setBelegDatum(_rs.getDate(NavisionDebitorOPConstants.BELEGDATUM));
            _record.setBelegNummer(_rs.getString(NavisionDebitorOPConstants.BELEGNUMMER));            
            _record.setBelegText(_rs.getString(NavisionDebitorOPConstants.BELEGTEXT));
            _record.setKonto(_rs.getString(NavisionDebitorOPConstants.KONTO));
            _record.setGegenKonto(NavisionDebitorOPConstants.GEGENKONTO);
            _record.setBetrag(_rs.getString(NavisionDebitorOPConstants.BETRAG));
            _record.setUstSchluessel(NavisionDebitorOPConstants.UMSATZSTEUERSCHLUESSEL);
            _record.setSkonto(NavisionDebitorOPConstants.SKONTO);
            _record.setKostenstelle(NavisionDebitorOPConstants.KOSTENSTELLE);
            _record.setKosteenstelleVomKonto(NavisionDebitorOPConstants.KOST_VON_KONTO);
            _record.setKundenLieferantenNummer(_record.getKonto());

            r_list.add(_record);
        }



		ApplicationLogger.getInstance().getLogger().info(
				"Quering Debitor OP = OK"
		);

        this.close();

		return r_list;

    }

    public List getAllInitizalKreditorOPAccountingRecords() throws SQLException{

            // *******************************************************
            // local variavles
            List r_list;
            String _query;
            ResultSet _rs;
            Statement _stmt;
            // *******************************************************

            // *******************************************************
            // init
            r_list = new LinkedList();
            _query = "SELECT "+
                    NavisionKreditorOPConstants.BELEGDATUM +         "," +
                    NavisionKreditorOPConstants.BELEGNUMMER +         "," +
                    NavisionKreditorOPConstants.BELEGTEXT +         "," +
                    NavisionKreditorOPConstants.KONTO + "," +
                    NavisionKreditorOPConstants.BETRAG +
                    " FROM "+
                    NavisionKreditorOPConstants.OP_KREDITORFILE;
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

            try{
                _rs = _stmt.executeQuery(_query);
            }
            catch(SQLException ex){
                ApplicationLogger.getInstance().getLogger().severe(
                        "Query File "+NavisionKreditorOPConstants.OP_KREDITORFILE+" failed :(" + ex
                );
                ApplicationLogger.getInstance().getLogger().severe(
                        "Query = "+ _query
                );
                throw ex;
            }

            while (_rs.next()){

                AccountingRecord _record;
                _record = new AccountingRecord();

                _record.setBelegDatum(_rs.getDate(NavisionKreditorOPConstants.BELEGDATUM));
                _record.setBelegNummer(_rs.getString(NavisionKreditorOPConstants.BELEGNUMMER));
                _record.setBelegText(_rs.getString(NavisionKreditorOPConstants.BELEGTEXT));
                _record.setKonto(_rs.getString(NavisionKreditorOPConstants.KONTO));
                _record.setGegenKonto(NavisionKreditorOPConstants.GEGENKONTO);
                _record.setBetrag(_rs.getString(NavisionKreditorOPConstants.BETRAG));
                _record.setUstSchluessel(NavisionKreditorOPConstants.UMSATZSTEUERSCHLUESSEL);
                _record.setSkonto(NavisionKreditorOPConstants.SKONTO);
                _record.setKostenstelle(NavisionKreditorOPConstants.KOSTENSTELLE);
                _record.setKosteenstelleVomKonto(NavisionKreditorOPConstants.KOST_VON_KONTO);
                _record.setKundenLieferantenNummer(_record.getKonto());

                r_list.add(_record);
            }



            ApplicationLogger.getInstance().getLogger().info(
                    "Quering Kreditor OP = OK"
            );

            this.close();

            return r_list;

        }


}
