package ctvdkip.database.voks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.Test;

class AccountingRecordTest {

   @Test
   void shouldTrimAndSanitizeBelegText() {
      final AccountingRecord record = new AccountingRecord();
      record.setBelegText("  abc'def  ");

      assertEquals("abc def", record.getBelegText());
   }

   @Test
   void shouldTruncateBelegTextToTwentyNineCharacters() {
      final AccountingRecord record = new AccountingRecord();
      record.setBelegText("12345678901234567890123456789012345");

      assertEquals("12345678901234567890123456789", record.getBelegText());
   }

   @Test
   void shouldCleanDotsFromBetragString() {
      final AccountingRecord record = new AccountingRecord();
      record.setBetrag("1.234.56");

      assertEquals("123456", record.getBetrag());
   }

   @Test
   void shouldFormatDateFromSqlDate() {
      final AccountingRecord record = new AccountingRecord();
      record.setBelegDatum(Date.valueOf("2024-01-07"));

      assertEquals("07.01.2024", record.getBelegDatum());
   }

   @Test
   void shouldSanitizeAccountFields() {
      final AccountingRecord record = new AccountingRecord();
      record.setKonto("'1000'");
      record.setGegenKonto("'2000'");

      assertEquals(" 1000 ", record.getKonto());
      assertEquals(" 2000 ", record.getGegenKonto());
   }
}
