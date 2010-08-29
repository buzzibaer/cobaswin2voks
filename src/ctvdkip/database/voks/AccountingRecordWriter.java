package ctvdkip.database.voks;

import ctvdkip.util.ApplicationLogger;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

/**
 * Created by IntelliJ IDEA.
 * User: rbust
 * Date: 24.08.2004
 * Time: 21:23:00
 */
public class AccountingRecordWriter {

    public boolean writeInitialAccountingRecordsToFile(List p_accountingrecords){

        FileWriter _filewriter;
        BufferedWriter _bufferedwriter;
        SimpleDateFormat fmt;
        SimpleDateFormat fmt2;
        Calendar cal;

        fmt = new SimpleDateFormat();
        fmt2 = new SimpleDateFormat();

        fmt2.applyPattern("dd.MM.yyyy");
        fmt.applyPattern("yyyyMMddkkmmss");
        cal = new GregorianCalendar();


        try {
            _filewriter = new FileWriter("Buchungsstapel_OP_" + fmt.format(cal.getTime()) +".txt");
        } catch (IOException e) {
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be created :("
			);
            return false;
        }
        _bufferedwriter = new BufferedWriter(_filewriter);

        try{
            _bufferedwriter.write("$iks$#"+fmt2.format(cal.getTime())+"#FIBU#FIBU");
            _bufferedwriter.newLine();

        }
        catch(IOException io){
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be saved :("
			);
            return false;
        }

        for (Iterator it = p_accountingrecords.iterator();it.hasNext();){

            AccountingRecord _tmprecord = (AccountingRecord) it.next();
            try {
                _bufferedwriter.write(
                        _tmprecord.getBelegDatum() +  ":" +
                        _tmprecord.getBelegNummer() +  ":" +
                        _tmprecord.getBelegText() + ":" +
                        _tmprecord.getKonto() +  ":" +
                        _tmprecord.getGegenKonto() +  ":" +
                        _tmprecord.getBetrag() +  ":" +
                        _tmprecord.getUstSchluessel() +  ":" +
                        _tmprecord.getSkonto() + ":" +
                        _tmprecord.getKostenstelle() +  ":" +
                        _tmprecord.getKosteenstelleVomKonto() + ":" +
                        ":::::1"
                        );
                _bufferedwriter.newLine();
            } catch (IOException e) {
                ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be saved :("
			);

            return false;
            }


        }

        try {
            _bufferedwriter.close();
        } catch (IOException e) {
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be closed :(" + e
			);
            return false;

        }

        return true;

    }

    public List generateAccountingRecords(List p_voksrecords){

            LinkedList r_list;

            r_list = new LinkedList();

            for(Iterator it = p_voksrecords.iterator(); it.hasNext();){

                VoksRecord _voksrecord;
                _voksrecord = (VoksRecord) it.next();

                if (_voksrecord instanceof VoksDebitorRecord){
                    //found debitor element
                    AccountingRecord _tmprecord;
                    _tmprecord = new AccountingRecord();

                    SimpleDateFormat fmt;
                    fmt = new SimpleDateFormat();
                    fmt.applyPattern("dd.MM.yyyy");

                    Calendar cal;
                    cal = new GregorianCalendar();

                    _tmprecord.setBelegDatum(fmt.format(cal.getTime()));
                    _tmprecord.setBelegNummer("INIT");
                    _tmprecord.setBelegText("SALDENUEBERNAHME AUS NAVISION");
                    _tmprecord.setKonto("4400");
                    _tmprecord.setGegenKonto(_voksrecord.getKundenNr());
                    _tmprecord.setKundenLieferantenNummer(_voksrecord.getKundenNr());

                    r_list.add(_tmprecord);

                }
                else if (_voksrecord instanceof VoksKreditorRecord){
                    //found kreditor element

                    AccountingRecord _tmprecord;
                    _tmprecord = new AccountingRecord();

                    SimpleDateFormat fmt;
                    fmt = new SimpleDateFormat();
                    fmt.applyPattern("dd.MM.yyyy");

                    Calendar cal;
                    cal = new GregorianCalendar();

                    _tmprecord.setBelegDatum(fmt.format( cal.getTime() ));
                    _tmprecord.setBelegNummer("INIT");
                    _tmprecord.setBelegText("SALDENUEBERNAHME AUS NAVISION");
                    _tmprecord.setKonto("4400");
                    _tmprecord.setGegenKonto(_voksrecord.getLieferantenNr());
                    _tmprecord.setKundenLieferantenNummer(_voksrecord.getLieferantenNr());

                    r_list.add(_tmprecord);
                }
                else{
                    ApplicationLogger.getInstance().getLogger().severe("Error while generating Accounting Records");

                }


            }


            return r_list;
        }

    public boolean writeCobasWinAccountingRecordsToFile(List p_accountingrecords){

        FileWriter _filewriter;
        BufferedWriter _bufferedwriter;
        SimpleDateFormat fmt;
        SimpleDateFormat fmt2;
        Calendar cal;

        fmt = new SimpleDateFormat();
        fmt2 = new SimpleDateFormat();

        fmt2.applyPattern("dd.MM.yyyy");
        fmt.applyPattern("yyyyMMddkkmmss");
        cal = new GregorianCalendar();


        try {
            _filewriter = new FileWriter("Buchungsstapel_" + fmt.format(cal.getTime()) +".txt");
        } catch (IOException e) {
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be created :("
			);
            return false;
        }
        _bufferedwriter = new BufferedWriter(_filewriter);

        try{
            _bufferedwriter.write("$iks$#"+fmt2.format(cal.getTime())+"#FIBU#FIBU");
            _bufferedwriter.newLine();

        }
        catch(IOException io){
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be saved :("
			);
            return false;
        }

        for (Iterator it = p_accountingrecords.iterator();it.hasNext();){

            AccountingRecord _tmprecord = (AccountingRecord) it.next();
            try {
                _bufferedwriter.write(
                        _tmprecord.getBelegDatum() +  ":" +
                        _tmprecord.getBelegNummer() +  ":" +
                        _tmprecord.getBelegText() + ":" +
                        _tmprecord.getKonto() +  ":" +
                        _tmprecord.getGegenKonto() +  ":" +
                        _tmprecord.getBetrag() +  ":" +
                        _tmprecord.getUstSchluessel() +  ":" +
                        _tmprecord.getSkonto() + ":" +
                        _tmprecord.getKostenstelle() +  ":" +
                        _tmprecord.getKosteenstelleVomKonto() + ":" +
                         ":::::1"
//                        _tmprecord.getKundenLieferantenNummer()
                        );
                _bufferedwriter.newLine();
            } catch (IOException e) {
                ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be saved :("
			);

            return false;
            }


        }

        try {
            _bufferedwriter.flush();
            _bufferedwriter.close();
        } catch (IOException e) {
            ApplicationLogger.getInstance().getLogger().severe(
					"File for Accounting Records could not be closed :(" + e
			);
            return false;

        }

        return true;

    }

}
