package ctvdkip.database.voks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VoksKreditorRecordTest {

   @Test
   void shouldDefaultPaymentCodeToZero() {
      final VoksKreditorRecord record = new VoksKreditorRecord();

      assertEquals("0", record.getZahlungsBedingungsCode());
   }

   @Test
   void shouldKeepDefaultPaymentCodeWhenInputIsNull() {
      final VoksKreditorRecord record = new VoksKreditorRecord();
      record.setZahlungsBedingungsCode(null);

      assertEquals("0", record.getZahlungsBedingungsCode());
   }

   @Test
   void shouldSanitizeEigeneKundennummer() {
      final VoksKreditorRecord record = new VoksKreditorRecord();
      record.setEigeneKundenNr("AB'12");

      assertEquals("AB 12", record.getEigeneKundenNr());
   }
}
