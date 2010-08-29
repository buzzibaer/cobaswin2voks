package ctvdkip.database.navision;

import ctvdkip.util.ApplicationLogger;
import java.sql.*;
import java.util.Vector;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;


/**
 *
 * navision Database Acces Class
 * 
 * @author rbust
 */
public class NavisionExelDB {
	
	/**
	 * The database connection object Debitor1
	 */
	private Connection dbConnection1;
	
	/**
	 * The database connection object Debitor2
	 */
	private Connection dbConnection2;
	
	/**
	 * dbConnectionstructor for navision DB
	 *
	 */
	public NavisionExelDB(){
	};
	
	/**
	 * 
	 * method is checking the given payment codes against the NavisionExelDB.
	 * import should be canceled if not equal
	 *
	 *
	 */
	public List getAllPaymentCodes() throws SQLException{
		
		String _query = "SELECT ZB FROM debitor2";
		ResultSet _rs;
		Statement _stmt;
		List _zahlbedausNavision = new LinkedList();
		
		
		this.open();
		
		try {
			_stmt = dbConnection2.createStatement();
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
			throw ex;
		}
		try{
			while (_rs.next()){
				
				Integer _int;
				_int = new Integer(_rs.getInt("ZB"));
/*
				ApplicationLogger.getInstance().getLogger().info(
						"ZB Value = "+ _int
				);
*/
				
				boolean found = false;
				
				for (Iterator enum = _zahlbedausNavision.iterator() ; enum.hasNext() ; ){
					if (_int.compareTo((Integer) enum.next()) == 0){
						//already exists leaving it untouched
/*
						ApplicationLogger.getInstance().getLogger().info(
								"ZB Value = "+ 
								_int + 
								" already exists, leavin it untouched"
						);
*/
						found = true;
						break;
					}
				}
				if (!found){
					_zahlbedausNavision.add(_int);
/*
					ApplicationLogger.getInstance().getLogger().info(
							"ZB Value = "+ 
							_int + 
							" is NEW, will be added"
					);
*/
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
		_stmt.close();
		this.close();
		
		ApplicationLogger.getInstance().getLogger().info(
				"Elements of Vector" + _zahlbedausNavision
		);
		
		return _zahlbedausNavision;

	};//end of method checkForPaymentCodes()
	
	/**
	 * 
	 * opens the dbConnectionnectino to the database
	 *
	 * @return boolean = false if dbConnectionnection could not be opend
	 */
	public boolean open(){
		
		// odbc string
		String _odbcstring = "sun.jdbc.odbc.JdbcOdbcDriver";
		String _dbConnectionString1 = "jdbc:odbc:Debitor1";
		String _dbConnectionString2 = "jdbc:odbc:Debitor2";
		//String _dbConnectionString1 = "jdbc:odbc:debitoren";
		//String _dbConnectionString2 = "jdbc:odbc:debitoren";

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
			dbConnection1 = DriverManager.getConnection(_dbConnectionString1);
			dbConnection2 = DriverManager.getConnection(_dbConnectionString2);
		}
		catch (SQLException p_sqlex ) {
			
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not open DB. DB is configured for exclusivly and maybe still open by ?"
			);
			return false;
		}
		
		ApplicationLogger.getInstance().getLogger().info(
				"Opening DB = OK"
		);	
		
		return true;
	};
	
	/**
	 *  function is closing database 
	 *
	 */
	public boolean close(){
		
		//closing DB Connection
		try{
			dbConnection1.close();	
			dbConnection2.close();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Could not close DB. Maybe it is already closed?"
			);
			return false;
		};
		
		ApplicationLogger.getInstance().getLogger().info(
				"Closing DB = OK"
		);	
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
	
	public ResultSet getAllDebitorData() throws SQLException{
		
		String _query = "SELECT * FROM debitor1";
		ResultSet _rs;
		Statement _stmt;
		
		try {
			_stmt = dbConnection1.createStatement();
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
		
		
		ApplicationLogger.getInstance().getLogger().info(
				"Quering navision DB for AllData = OK"
		);
		
		return _rs;
	}

	
	public ResultSet getRemainingDebitorData(String p_Kundennummer) throws SQLException{
		
		String _query = "SELECT * FROM debitor2 WHERE NUMMER = " + p_Kundennummer;
		ResultSet _rs;
		Statement _stmt;
		
		try {
			_stmt = dbConnection2.createStatement();
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
		
		
		ApplicationLogger.getInstance().getLogger().info(
				"Quering navision DB for RemainingData = OK"
		);
		
		
		return _rs;
	}
	
	/**
	 * method is checking consistens of Debitor1 and Debitor2 File.
	 * Counting cusumor ids and checking which are corrupt.
	 * 
	 * @return  true if files consistent
	 */
	public boolean checkConsistentDebitorFiles() throws SQLException{
		
		//*****************************************************
		// local variables
		//*****************************************************
		Vector _fromFile1 = new Vector();
		Vector _fromFile2 = new Vector();
		String _query1 = "SELECT * FROM debitor1";
		String _query2 = "SELECT * FROM debitor2";
		ResultSet _rs1;
		ResultSet _rs2;
		Statement _stmt1;
		Statement _stmt2;
		//*****************************************************
		
		this.open();
		
		
		try {
			_stmt1 = dbConnection1.createStatement();
			_stmt2 = dbConnection2.createStatement();
		}
		catch(SQLException p_sqlex){
			ApplicationLogger.getInstance().getLogger().severe(
					"DB Statement could not be created :("
			);
			throw p_sqlex;
		}
		
		try{
			_rs1 = _stmt1.executeQuery(_query1);
			_rs2 = _stmt2.executeQuery(_query2);
		}
		catch(SQLException ex){
			ApplicationLogger.getInstance().getLogger().severe(
					"Query the navision DB failed :(" + ex
			);
			throw ex;
		}
		
		while(_rs1.next()){
			
			Integer _kundennummer;
			
			_kundennummer = new Integer(_rs1.getInt(1));
			
			boolean found = false;
			
			for(java.util.Enumeration enum = _fromFile1.elements();enum.hasMoreElements();){
				if (0 == _kundennummer.compareTo((Integer) enum.nextElement())){
					//already exists leaving it untouched
					ApplicationLogger.getInstance().getLogger().severe(
							"Kundennummer Value = "+ 
							_kundennummer + 
							" already exists, duplicate entry found in Debitor1 :("
					);
					found = true;
					break;
				}
				
			}
			if (!found){
				_fromFile1.add(_kundennummer);
/*
				ApplicationLogger.getInstance().getLogger().info(
						"Kundnenummer Value = "+ 
						_kundennummer + 
						" is NEW, will be added"
				);
*/
			}
			found = false;
		}
		
		while(_rs2.next()){
			
			Integer _kundennummer;
			
			_kundennummer = new Integer(_rs2.getInt(1));
			
			boolean found = false;
			
			for(java.util.Enumeration enum = _fromFile2.elements();enum.hasMoreElements();){
				if (0 == _kundennummer.compareTo((Integer) enum.nextElement())){
					//already exists leaving it untouched
					ApplicationLogger.getInstance().getLogger().severe(
							"Kundennummer Value = "+ 
							_kundennummer + 
							" already exists, duplicate entry found in Debitor1 :("
					);
					found = true;
					break;
				}
			
			}
			if (!found){
				_fromFile2.add(_kundennummer);
				/*
				 ApplicationLogger.getInstance().getLogger().info(
				 "Kundnenummer Value = "+ 
				 _kundennummer + 
				 " is NEW, will be added"
				 );
				 */
			}
			found = false;
		}
		
		
		if(!this.getOverlapElements(_fromFile1,_fromFile2).isEmpty()){

			ApplicationLogger.getInstance().getLogger().severe(
				"Found Overlap Elements in Debitor Files"
			);
			return false;
		}
		
		_stmt1.close();
		_stmt2.close();
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
							"Element NOT found in Vector adding to retrun Vector"
					);
					
					r_list.add(_IntegerOne);
				}
			}
		}
		else if (_one.size() < _two.size()){
			
			ApplicationLogger.getInstance().getLogger().info(
					"Vector _two > _one"
			);
			
			for (Iterator eone = _two.iterator();eone.hasNext();){
				
				Integer _IntegerOne = (Integer) eone.next();
				
				for (Iterator etwo = _one.iterator(); etwo.hasNext();){
					
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
							"Element NOT found in Vector adding to retrun Vector"
					);
					
					r_list.add(_IntegerOne);
				}
			}
		}else{
			ApplicationLogger.getInstance().getLogger().info(
					"Size of Vector are equal. Return Vector will be size =0"
			);
		}

        if (!r_list.isEmpty()){
            ApplicationLogger.getInstance().getLogger().severe(
                    "Overlap Elements of Vector" + r_list
            );
        }
		return r_list;
	}
};
