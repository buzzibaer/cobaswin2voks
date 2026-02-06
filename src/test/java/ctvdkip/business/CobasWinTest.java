package ctvdkip.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;

class CobasWinTest {

   @Test
   void shouldSplitDebitorsIntoNewAndUpdate() {
      final CobasWin cobasWin = new CobasWin();

      final VoksDebitorRecord existing = new VoksDebitorRecord();
      existing.setKundenNr("10001");

      final VoksDebitorRecord toUpdate = new VoksDebitorRecord();
      toUpdate.setKundenNr("10001");

      final VoksDebitorRecord toInsert = new VoksDebitorRecord();
      toInsert.setKundenNr("10002");

      final List<List<VoksDebitorRecord>> split = cobasWin.splitIntoUpdateAndInsert(
            Arrays.asList(toUpdate, toInsert),
            Arrays.asList(existing));

      assertEquals(1, split.get(0).size());
      assertEquals("10002", split.get(0).get(0).getKundenNr());
      assertEquals(1, split.get(1).size());
      assertEquals("10001", split.get(1).get(0).getKundenNr());
   }

   @Test
   void shouldSplitKreditorsIntoNewAndUpdate() {
      final CobasWin cobasWin = new CobasWin();

      final VoksKreditorRecord existing = new VoksKreditorRecord();
      existing.setLieferantenNr("70001");

      final VoksKreditorRecord toUpdate = new VoksKreditorRecord();
      toUpdate.setLieferantenNr("70001");

      final VoksKreditorRecord toInsert = new VoksKreditorRecord();
      toInsert.setLieferantenNr("70002");

      final List<List<VoksKreditorRecord>> split = cobasWin.splitIntoUpdateAndInsert(
            Arrays.asList(toUpdate, toInsert),
            Arrays.asList(existing));

      assertEquals(1, split.get(0).size());
      assertEquals("70002", split.get(0).get(0).getLieferantenNr());
      assertEquals(1, split.get(1).size());
      assertEquals("70001", split.get(1).get(0).getLieferantenNr());
   }

   @Test
   void shouldMigrateAccountNumbersFromSevenToSixDigits() {
      final VoksDebitorRecord debitor = new VoksDebitorRecord();
      debitor.setKundenNr("1234567");

      final VoksKreditorRecord kreditor = new VoksKreditorRecord();
      kreditor.setLieferantenNr("7654321");

      CobasWin.migrateAccountNumbersFrom7To6(Arrays.asList(debitor, kreditor));

      assertEquals("234567", debitor.getKundenNr());
      assertEquals("654321", kreditor.getLieferantenNr());
   }

   @Test
   void shouldMigrateAccountingRecordsFromSevenToSixDigits() {
      final AccountingRecord record = new AccountingRecord();
      record.setKonto("1234567");
      record.setGegenKonto("2345678");

      CobasWin.migrateAccountRecordsFrom7to6Digits(Arrays.asList(record));

      assertEquals("234567", record.getKonto());
      assertEquals("345678", record.getGegenKonto());
   }

   @Test
   void shouldMigrateAccountingRecordsFromFourToSixDigits() {
      final AccountingRecord record = new AccountingRecord();
      record.setKonto("1234");
      record.setGegenKonto("4567");

      CobasWin.migrateAccountRecordsFrom4To6Digits(Arrays.asList(record));

      assertEquals("123400", record.getKonto());
      assertEquals("456700", record.getGegenKonto());
   }
}
