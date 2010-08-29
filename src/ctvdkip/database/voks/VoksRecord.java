package ctvdkip.database.voks;

import ctvdkip.util.ApplicationLogger;

/**
 * User: rbust
 * Date: 21.08.2004
 * Time: 11:43:16
 * Gemeinsame Elemente aus der Tabelle VFAdress werden hier zusammengefasst
 */
public abstract class VoksRecord {

    private String Name;
    private String KundenNr;
    private String Zusatz;
    private String Strasse;
    private String Land;
    private String PLZ;
    private String Ort;
    private String Telefon;
    private String Fax;
    private String Zahlungsverkehr;
    private String Bankname;
    private String Bankleitzahl;
    private String BankKontoNr;
    protected String ZahlungsBedingungsCode; 	//Zahlungsbedingungen
    private String Email;
    private String Saldo;

    public String getLieferantenNr() {
        return LieferantenNr;
    }

    public void setLieferantenNr(String p_lieferantenNr) {

        if (null == p_lieferantenNr) {
            this.LieferantenNr = "";
        } else {
            this.LieferantenNr = p_lieferantenNr.trim();
        }

        this.LieferantenNr = cleanString(this.LieferantenNr, "'", " ");

    }

    private String LieferantenNr;

    public VoksRecord() {

        KundenNr = "";
        LieferantenNr = "";
        Name = "";
        Zusatz = "";
        Strasse = "";
        Land = "";
        PLZ = "00000";
        Ort = "";
        Telefon = "";
        Fax = "";
        Zahlungsverkehr = "";
        Bankname = "";
        Bankleitzahl = "";
        BankKontoNr = "";
        ZahlungsBedingungsCode="2"; 	//Zahlungsbedingungen     //DEFUALT = 2
        Email ="";
        Saldo = "";
    }

    /**
     * @param p_Kundennr
     */
    public void setKundenNr(String p_Kundennr) {

        if (null == p_Kundennr) {
            this.KundenNr = "";
        } else {
            this.KundenNr = p_Kundennr.trim();
        }

        this.KundenNr = cleanString(this.KundenNr, "'", " ");

    }

    /**
     * @return Returns the kundennr.
     */
    public String getKundenNr() {
        return KundenNr;
    }

    public void setKundenNr(int _int) {

        this.KundenNr = String.valueOf(_int);

    }

    public void setLieferantenNr(int _int) {

        this.LieferantenNr = String.valueOf(_int);

    }

    /**
     * method for cleaning something out of a string
     *
     * @param s
     * @param search
     * @param replace
     * @return cleaned string
     */
    protected String cleanString(String s, String search, String replace) {
        StringBuffer s2 = new StringBuffer();
        int i = 0, j = 0;
        int len = search.length();

        while (j > -1) {
            j = s.indexOf(search, i);

            if (j > -1) {
                s2.append(s.substring(i, j));
                s2.append(replace);
                i = j + len;
            }
        }
        s2.append(s.substring(i, s.length()));

        return s2.toString();
    }

    protected boolean compareTo(String p_KundenNr,
                                String p_name,
                                String p_zusatz,
                                String p_strasse,
                                String p_land,
                                String p_plz,
                                String p_ort,
                                String p_telefon,
                                String p_fax,
                                String p_zahlungsverkehr,
                                String p_bankname,
                                String p_bankleitzahl,
                                String p_bankkontonummer) {

        if (!this.KundenNr.equalsIgnoreCase(p_KundenNr)) {
            return false;
        } else if (!this.KundenNr.equalsIgnoreCase(p_name)) {
            return false;
        } else if (!this.Zusatz.equalsIgnoreCase(p_zusatz)) {
            return false;
        } else if (!this.Strasse.equalsIgnoreCase(p_strasse)) {
            return false;
        } else if (!this.Land.equalsIgnoreCase(p_land)) {
            return false;
        } else if (!this.PLZ.equalsIgnoreCase(p_plz)) {
            return false;
        } else if (!this.Ort.equalsIgnoreCase(p_ort)) {
            return false;
        } else if (!this.Telefon.equalsIgnoreCase(p_telefon)) {
            return false;
        } else if (!this.Fax.equalsIgnoreCase(p_fax)) {
            return false;
        } else if (!this.Zahlungsverkehr.equalsIgnoreCase(p_zahlungsverkehr)) {
            return false;
        } else if (!this.Bankname.equalsIgnoreCase(p_bankname)) {
            return false;
        } else if (!this.Bankleitzahl.equalsIgnoreCase(p_bankleitzahl)) {
            return false;
        } else if (!this.BankKontoNr.equalsIgnoreCase(p_bankkontonummer)) {
            return false;
        }

        return true;
    }

    public void setName(String p_name) {

        if (null == p_name) {
            this.Name = "";
        } else {
            this.Name = p_name.trim();
        }
        this.Name = cleanString(this.Name, "'", " ");
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return Name;
    }

    public void setZusatz(String p_zusatz) {

        if (null == p_zusatz) {
            this.Zusatz = "";
        } else {
            this.Zusatz = p_zusatz.trim();
        }
        this.Zusatz = cleanString(this.Zusatz, "'", " ");
    }

    /**
     * @return Returns the zusatz.
     */
    public String getZusatz() {
        return Zusatz;
    }

    public void setStrasse(String p_strasse) {

        if (null == p_strasse) {
            this.Strasse = "";
        } else {
            this.Strasse = p_strasse.trim();
        }
        this.Strasse = cleanString(this.Strasse, "'", " ");


    }

    /**
     * @return Returns the strasse.
     */
    public String getStrasse() {
        return Strasse;
    }

    public void setLand(String p_land) {

        if (null == p_land) {
            this.Land = "";
        } else {
            this.Land = p_land.trim();
        }
        this.Land = cleanString(this.Land, "'", " ");

    }

    /**
     * @return Returns the land.
     */
    public String getLand() {
        return Land;
    }


    public void setBankKontoNr(String p_KontoNr) {

        if (p_KontoNr == null) {
            this.BankKontoNr = "";
        } else {
            this.BankKontoNr = p_KontoNr.trim();
        }
        this.BankKontoNr = cleanString(this.BankKontoNr, "'", " ");
    }

    public void setBankLeitZahl(String p_blz) {

        if (p_blz == null) {
            this.Bankleitzahl = "";
        } else {
            this.Bankleitzahl = p_blz.trim();
        }
        this.Bankleitzahl = cleanString(this.Bankleitzahl, "'", " ");

    }

    public void setBankName(String p_bankname) {

        if (p_bankname == null) {
            this.Bankname = "";
        } else {
            this.Bankname = p_bankname.trim();
        }

        this.Bankname = cleanString(this.Bankname, "'", " ");

    }

    public void setFaxNummer(String p_faxnummer) {

        if (null == p_faxnummer) {
            this.Fax = "";
        } else {
            this.Fax = p_faxnummer.trim();
        }
        this.Fax = cleanString(this.Fax, "'", " ");

    }


    public void setOrt(String p_ort) {

        if (null == p_ort) {
            this.Ort = "";
        } else {
            this.Ort = p_ort.trim();
        }
        this.Ort = cleanString(this.Ort, "'", " ");


    }

    public void setPlz(String p_plz) {

        if (null == p_plz) {
            this.PLZ = "00000";
        } else {
            this.PLZ = p_plz.trim();
        }
        this.PLZ = cleanString(this.PLZ, "'", " ");

    }


    public void setTelefon(String p_telefon) {

        if (null == p_telefon) {
            this.Telefon = "";
        } else {
            this.Telefon = p_telefon.trim();
        }
        this.Telefon = cleanString(this.Telefon, "'", " ");

    }

    public void setZahlungsverkehr(String p_zahlungsverkehr) {

        this.Zahlungsverkehr = p_zahlungsverkehr.trim();

        //checking for zahlungsverkehr conditions
        // allowed are the following states by voks:
        // ALGBN
        // A = Abbucher
        // L = Lastschrift
        // G = Gutschrift
        // B = Lastschrift und Gutschrift
        // N = Nix

        if (this.Zahlungsverkehr.compareTo("A") == 0) {
            //ApplicationLogger.getInstance().getLogger().info("Zahlungsverkehr = Abbucher");

        } else if (this.Zahlungsverkehr.compareTo("L") == 0) {
            //ApplicationLogger.getInstance().getLogger().info("Zahlungsverkehr = Lastschrift");

        } else if (this.Zahlungsverkehr.compareTo("G") == 0) {
            //ApplicationLogger.getInstance().getLogger().info("Zahlungsverkehr = Gutschrift");

        } else if (this.Zahlungsverkehr.compareTo("B") == 0) {
            //ApplicationLogger.getInstance().getLogger().info("Zahlungsverkehr = Lastschrift + Gutschrift");

        } else if (this.Zahlungsverkehr.compareTo("N") == 0) {

        } else {
            ApplicationLogger.getInstance().getLogger().severe("Zahlungsverkehr nicht definiert. Gelesen = " +
                    this.Zahlungsverkehr);
            ApplicationLogger.getInstance().getLogger().severe("Reverting to Standard = N" +
                    this.Zahlungsverkehr);
            this.Zahlungsverkehr = "N";

        }

    }

    /**
     * @return Returns the bankKontoNr.
     */
    public String getBankKontoNr() {
        return BankKontoNr;
    }

    /**
     * @return Returns the bankleitzahl.
     */
    public String getBankleitzahl() {
        return Bankleitzahl;
    }

    /**
     * @return Returns the bankname.
     */
    public String getBankname() {
        return Bankname;
    }

    /**
     * @return Returns the fax.
     */
    public String getFax() {
        return Fax;
    }


    /**
     * @return Returns the ort.
     */
    public String getOrt() {
        return Ort;
    }

    /**
     * @return Returns the pLZ.
     */
    public String getPLZ() {
        return PLZ;
    }


    /**
     * @return Returns the telefon.
     */
    public String getTelefon() {
        return Telefon;
    }

    /**
     * @return Returns the zahlungsverkehr.
     */
    public String getZahlungsverkehr() {
        return Zahlungsverkehr;
    }

    /**
	 * @return Returns the zB.
	 */
	public String getZahlungsBedingungsCode() {
		return ZahlungsBedingungsCode;
	}

    public abstract void setZahlungsBedingungsCode(String p_zbcode);

    public void setEmail(String p_email){

        if (null == p_email) {
            this.Email = "";
        } else {
            this.Email = p_email.trim();
        }
        this.Email = cleanString(this.Email, "'", " ");

    }

    public String getEmail(){
        return Email;
    }

    public void setSaldo(String p_saldo){

        if (null == p_saldo) {
            this.Saldo = "";
        } else {
            this.Saldo = p_saldo.trim();
        }
        this.Saldo = cleanString(this.Saldo, "'", " ");

    }

    public String getSaldo(){
        return Saldo;
    }
}
