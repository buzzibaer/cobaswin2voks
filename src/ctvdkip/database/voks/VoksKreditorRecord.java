package ctvdkip.database.voks;

import ctvdkip.database.voks.VoksDebitorRecord;

/**
 * Created by IntelliJ IDEA.
 * User: rbust
 * Date: 21.08.2004
 * Time: 11:12:35
 * To change this template use File | Settings | File Templates.
 */
public class VoksKreditorRecord extends VoksRecord{


    private String EigeneKundenNr;

    /**
     * constuctor
     *
     */
    public VoksKreditorRecord(){
        super();
        EigeneKundenNr = "";
        ZahlungsBedingungsCode="0"; 	//Zahlungsbedingungen     //DEFAULT = 0
    }

    public void setEigeneKundenNr(String p_eigenekundennr){
        if (null == p_eigenekundennr){
            this.EigeneKundenNr = "";
        }
        else{
            this.EigeneKundenNr = p_eigenekundennr.trim();
        }
        this.EigeneKundenNr = cleanString(this.EigeneKundenNr,"'"," ");
    }

    public String getEigeneKundenNr(){
        return this.EigeneKundenNr;
    }



    public void setZahlungsBedingungsCode(String p_zb){

        if (null == p_zb){
            super.ZahlungsBedingungsCode = "";
        }
        else{
            super.ZahlungsBedingungsCode = p_zb.trim();
        }
        this.ZahlungsBedingungsCode = cleanString(this.ZahlungsBedingungsCode,"'"," ");
        if(super.ZahlungsBedingungsCode.equalsIgnoreCase("")){
            //setting default value
            super.ZahlungsBedingungsCode = "0";
        }
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
     * @return returns true if internal value of objects are equal
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



        //ZahlungsBedingungsCode (Zahlungsbedingungen)
        if(!this.ZahlungsBedingungsCode.equalsIgnoreCase(_compare.getZahlungsBedingungsCode().trim())){
            return false;
        }

        return true;
    }

}
