/*
 * Created on 16.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ctvdkip.database.voks;


/**
 * @author rbust
 * 
 */
public class VoksDebitorRecord extends VoksRecord {



	private String UstID;

	/**
	 * constuctor
	 *
	 */
	public VoksDebitorRecord(){
        super();
        UstID="";

	}
	
	

	
	public void setUstID(String p_ustid){

		if (p_ustid == null){
            this.UstID ="";
        }
        else{
            this.UstID = p_ustid.trim();
        }
        this.UstID = cleanString(this.UstID,"'"," ");

	}
	

	

	
	public void setZahlungsBedingungsCode(String p_zb){

		if (null == p_zb){
            super.ZahlungsBedingungsCode = "";
        }
        else{
            super.ZahlungsBedingungsCode = p_zb.trim();
        }
        super.ZahlungsBedingungsCode = cleanString(super.ZahlungsBedingungsCode,"'"," ");
        if(super.ZahlungsBedingungsCode.equalsIgnoreCase("")){
            //setting default value
            super.ZahlungsBedingungsCode = "2";
        }
	}


	/**
	 * @return Returns the ustID.
	 */
	public String getUstID() {
		return UstID;
	}

	
	public boolean compareTo (VoksDebitorRecord _compare){

        //super compaer
        if(!super.compareTo(
                _compare.getKundenNr(),
                _compare.getName(),
                _compare.getZusatz(),
                _compare.getStrasse(),
                _compare.getLand(),
                _compare.getPLZ(),
                _compare.getOrt(),
                _compare.getTelefon(),
                _compare.getFax(),
                _compare.getZahlungsverkehr(),
                _compare.getBankname(),
                _compare.getBankleitzahl(),
                _compare.getBankKontoNr()
                ))
        {
            return false;
        }



		//UstID
		if(!this.UstID.equalsIgnoreCase(_compare.getUstID().trim())){
			return false;
		}

		//ZahlungsBedingungsCode (Zahlungsbedingungen)
		if(!this.ZahlungsBedingungsCode.equalsIgnoreCase(_compare.getZahlungsBedingungsCode().trim())){
			return false;
		}

		return true;
	}
	
	/**
	 * 
	 * Zahlungsverkehr + Bankname + Bankleitzahl +BankKontoNr will not be compared
	 * @param _compare
	 * @return true if objact internal values are equal
	 */
	public boolean compareToWithoutBankData (VoksDebitorRecord _compare){

		//super compaer
        if(!super.compareTo(
                _compare.getKundenNr(),
                _compare.getName(),
                _compare.getZusatz(),
                _compare.getStrasse(),
                _compare.getLand(),
                _compare.getPLZ(),
                _compare.getOrt(),
                _compare.getTelefon(),
                _compare.getFax(),
                _compare.getZahlungsverkehr(),
                _compare.getBankname(),
                _compare.getBankleitzahl(),
                _compare.getBankKontoNr()
                ))
        {
            return false;
        }


		//UstID
		if(!this.UstID.equalsIgnoreCase(_compare.getUstID().trim())){
			return false;
		}

		//ZahlungsBedingungsCode (Zahlungsbedingungen)
		if(!this.ZahlungsBedingungsCode.equalsIgnoreCase(_compare.getZahlungsBedingungsCode().trim())){
			return false;
		}

		return true;
	}


}
