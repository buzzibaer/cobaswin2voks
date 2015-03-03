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

    public boolean writeCobasWinAccountingRecordsToFile(List<AccountingRecord> p_accountingrecords){

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

        for (Iterator<AccountingRecord> it = p_accountingrecords.iterator();it.hasNext();){

            AccountingRecord _tmprecord = it.next();
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
