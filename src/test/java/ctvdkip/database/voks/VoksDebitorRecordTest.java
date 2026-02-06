package ctvdkip.database.voks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VoksDebitorRecordTest {

   @Test
   void shouldDefaultPaymentCodeToTwoWhenMissing() {
      final VoksDebitorRecord record = new VoksDebitorRecord();
      record.setZahlungsBedingungsCode(null);

      assertEquals("2", record.getZahlungsBedingungsCode());
   }

   @Test
   void shouldSanitizeUstId() {
      final VoksDebitorRecord record = new VoksDebitorRecord();
      record.setUstID("DE'123");

      assertEquals("DE 123", record.getUstID());
   }
}
