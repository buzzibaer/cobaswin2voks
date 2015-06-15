package ctvdkip.database.agenda;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;
import ctvdkip.util.ApplicationLogger;

public class AgendaRecordWriter {

   public boolean writeAgendaAccountingRecordsToFile(final List<AccountingRecord> p_accountingrecords) {
      final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddkkmmss");
      final Calendar cal = new GregorianCalendar();

      BufferedWriter writer = null;
      try {
         writer = new BufferedWriter(new FileWriter("Agenda_Buchungsstapel_OP_" + fmt.format(cal.getTime()) + ".csv"));

         //Header
         writer.write("Datum;Beleg1;Buchungstext;Konto;Gegenkonto;Umsatz in Euro;Steuerschl;Skonto in Euro;Kost1;Kost2;");
         writer.newLine();

         //Datensaetze
         for (final AccountingRecord record : p_accountingrecords) {
            final StringBuilder b = new StringBuilder();
            b.append(record.getBelegDatum());
            b.append(";");
            b.append(record.getBelegNummer());
            b.append(";");
            b.append(record.getBelegText());
            b.append(";");
            b.append(record.getKonto());
            b.append(";");
            b.append(record.getGegenKonto());
            b.append(";");
            b.append(record.getBetrag());
            b.append(";");
            b.append(record.getUstSchluessel());
            b.append(";");
            b.append(record.getSkonto());
            b.append(";");
            //b.append(record.getKostenstelle()); -- kostenstelle soll ins agenda nicht übernommen werden, dann fängt die automatik an zu spinnen
            b.append(";");
            //b.append(record.getKosteenstelleVomKonto()); -- kostenstelle soll ins agenda nicht übernommen werden, dann fängt die automatik an zu spinnen
            b.append(";");
            writer.write(b.toString());
            writer.newLine();
         }

         //flush
         writer.flush();

      } catch (final IOException ioEx) {
         ApplicationLogger.getInstance().getLogger().severe("Error on writing agenda accounting records: " + ioEx.getMessage());
         return false;
      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (final IOException ignore) {
            }
         }
      }

      return true;

   }

   public boolean writeAgendaPersonAccountsToFile(final List<VoksDebitorRecord> debitors, final List<VoksKreditorRecord> kreditors) {
      final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddkkmmss");
      final Calendar cal = new GregorianCalendar();

      BufferedWriter writer = null;
      try {
         writer = new BufferedWriter(new FileWriter("Agenda_Personenkonten_" + fmt.format(cal.getTime()) + ".csv"));

         //Header
         writer.write("Konto;Name1;Name2;Strasse;Land;PLZ;Ort;Telefon;Telefax;E-Mail;Zahlungsbedingung;EUID;");
         writer.newLine();

         //Debitoren
         for (final VoksDebitorRecord record : debitors) {
            final StringBuilder b = new StringBuilder();
            b.append(record.getKundenNr());
            b.append(";");
            b.append(record.getName());
            b.append(";");
            b.append(record.getZusatz());
            b.append(";");
            b.append(record.getStrasse());
            b.append(";");
            b.append(record.getLand());
            b.append(";");
            b.append(record.getPLZ());
            b.append(";");
            b.append(record.getOrt());
            b.append(";");
            b.append(record.getTelefon());
            b.append(";");
            b.append(record.getFax());
            b.append(";");
            b.append(record.getEmail());
            b.append(";");
            b.append(record.getZahlungsBedingungsCode());
            b.append(";");
            b.append(record.getUstID());
            b.append(";");
            writer.write(b.toString());
            writer.newLine();
         }

         //Kreditoren
         for (final VoksKreditorRecord record : kreditors) {
            final StringBuilder b = new StringBuilder();
            b.append(record.getLieferantenNr());
            b.append(";");
            b.append(record.getName());
            b.append(";");
            b.append(record.getZusatz());
            b.append(";");
            b.append(record.getStrasse());
            b.append(";");
            b.append(record.getLand());
            b.append(";");
            b.append(record.getPLZ());
            b.append(";");
            b.append(record.getOrt());
            b.append(";");
            b.append(record.getTelefon());
            b.append(";");
            b.append(record.getFax());
            b.append(";");
            b.append(record.getEmail());
            b.append(";");
            b.append(record.getZahlungsBedingungsCode());
            b.append(";");
            //b.append(record.getUstID()); -- kreditoren UStID wird direkt in der Buchhaltung angelegt, nicht aus CobasWin
            b.append(";");
            writer.write(b.toString());
            writer.newLine();
         }

         //flush
         writer.flush();

      } catch (final IOException ioEx) {
         ApplicationLogger.getInstance().getLogger().severe("Error on writing agenda person accounts: " + ioEx.getMessage());
         return false;
      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (final IOException ignore) {
            }
         }
      }

      return true;
   }
}
