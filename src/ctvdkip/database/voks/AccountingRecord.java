package ctvdkip.database.voks;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: rbust
 * Date: 24.08.2004
 * Time: 16:33:26
 * To change this template use File | Settings | File Templates.
 */
public class AccountingRecord {

    private String BelegDatum;
    private String BelegNummer;
    private String BelegText;
    private String Konto;
    private String GegenKonto;
    private String Betrag;
    private String UstSchluessel;
    private String Skonto;
    private String Kostenstelle;
    private String KosteenstelleVomKonto;
    private String KundenLieferantenNummer;
    private String Verarbeitet;
    private String RecordNummer;

    public String getBelegDatum() {
        return BelegDatum;
//        return "01.06.2004";      //@todo FLAG DATUM
    }

    public void setBelegDatum(String belegDatum) {
        if (null == belegDatum){
            BelegDatum = "";
        }
        else{
            BelegDatum = belegDatum.trim();
        }

    }

    public String getBelegNummer() {
        return BelegNummer;
    }

    public void setBelegNummer(String belegNummer) {
        if (null == belegNummer){
            BelegNummer = "";
        }
        else{
            BelegNummer = belegNummer.trim();
        }

    }

    public String getBelegText() {
        return BelegText;
    }

    public void setBelegText(String belegText) {
        if (null == belegText){
            BelegText = "";
        }
        else{
            BelegText = belegText.trim();
        }
        if (BelegText.length() >= 30){
            BelegText = BelegText.substring(0,29);

        }
        BelegText = this.cleanString(BelegText,"'"," ");

    }

    public String getKonto() {
        return Konto;
    }

    public void setKonto(String konto) {
        if (null == konto){
            Konto = "";
        }
        else{
            Konto = konto.trim();
        }
        Konto = this.cleanString(Konto,"'"," ");
    }

    public String getGegenKonto() {
        return GegenKonto;
    }

    public void setGegenKonto(String gegenKonto) {
        if (null == gegenKonto){
            GegenKonto = "";
        }
        else{
            GegenKonto = gegenKonto.trim();
        }
        GegenKonto = this.cleanString(GegenKonto,"'"," ");

    }

    public String getBetrag() {
        return Betrag;
    }

    public void setBetrag(String betrag) {

        if (null == betrag){
            Betrag = "";
        }
        else{
            Betrag = betrag.trim();
        }
        Betrag = this.cleanString(Betrag,"'"," ");
        Betrag = this.cleanString(Betrag,".","");

    }

    public void setBetrag(float p_betrag){

        DecimalFormat df = new DecimalFormat( "###########################0.00" );
        Betrag = df.format(p_betrag);

    }

    public String getUstSchluessel() {
        return UstSchluessel;
    }

    public void setUstSchluessel(String ustSchluessel) {
        if (null == ustSchluessel){
            UstSchluessel = "";
        }
        else{
            UstSchluessel = ustSchluessel.trim();
        }
        UstSchluessel = this.cleanString(UstSchluessel,"'"," ");
    }

    public String getSkonto() {
        return Skonto;
    }

    public void setSkonto(String skonto) {
        if (null == skonto){
            Skonto = "";
        }
        else{
            Skonto = skonto.trim();
        }
        Skonto = this.cleanString(Skonto,"'"," ");
    }

    public void setSkonto(float p_skonto){

        DecimalFormat df = new DecimalFormat( "###########################0.00" );
        Skonto = df.format(p_skonto);

    }

    public String getKostenstelle() {
        return Kostenstelle;
    }

    public void setKostenstelle(String kostenstelle) {
        if (null == kostenstelle){
            Kostenstelle = "";
        }
        else{
            Kostenstelle = kostenstelle.trim();
        }
        Kostenstelle = this.cleanString(Kostenstelle,"'"," ");
    }

    public String getKosteenstelleVomKonto() {
        return KosteenstelleVomKonto;
    }

    public void setKosteenstelleVomKonto(String kosteenstelleVomKonto) {
        if (null == kosteenstelleVomKonto){
            KosteenstelleVomKonto = "";
        }
        else{
            KosteenstelleVomKonto = kosteenstelleVomKonto.trim();
        }
        KosteenstelleVomKonto = this.cleanString(KosteenstelleVomKonto,"'"," ");


    }

    public String getKundenLieferantenNummer() {
        return KundenLieferantenNummer;
    }

    public void setKundenLieferantenNummer(String kundenLieferantenNummer) {
        if (null == kundenLieferantenNummer){
            KundenLieferantenNummer = "";
        }
        else{
            KundenLieferantenNummer = kundenLieferantenNummer.trim();
        }
        KundenLieferantenNummer = this.cleanString(KundenLieferantenNummer,"'"," ");
    }

    public void setVerarbeitet(String p_verarbeitet){
        if (null == p_verarbeitet){
            Verarbeitet = "";
        }
        else{
            Verarbeitet = p_verarbeitet.trim();
        }
        Verarbeitet = this.cleanString(Verarbeitet,"'"," ");      
    }

    public String getVerarbeitet(){
        return Verarbeitet;
    }

    public void setRecordNummer(String p_recordnummer){
        if (null == p_recordnummer){
            RecordNummer = "";
        }
        else{
            RecordNummer = p_recordnummer.trim();
        }
        RecordNummer = this.cleanString(RecordNummer,"'"," ");
    }

    public String getRecordNummer(){
        return RecordNummer;
    }

    public void setBelegDatum(Date p_date){

        if(null == p_date){
            this.BelegDatum = "";
        }
        else{
            SimpleDateFormat fmt;

            fmt = new SimpleDateFormat();
            fmt.applyPattern("dd.MM.yyyy");

            this.BelegDatum = fmt.format(p_date);
        }



    }

    public AccountingRecord() {

        BelegDatum = "";
        BelegNummer = "";
        BelegText = "";
        Konto = "";
        GegenKonto = "";
        Betrag = "";
        UstSchluessel = "";
        Skonto = "";
        Kostenstelle = "";
        KosteenstelleVomKonto = "";
        KundenLieferantenNummer = "";
        Verarbeitet = "";
        RecordNummer = "";
        RecordNummer = "";
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
}
