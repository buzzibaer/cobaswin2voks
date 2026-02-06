package ctvdkip.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PaymentCodesTest {

   @Test
   void shouldResolvePaymentCodeById() {
      assertEquals(PaymentCodes.PC10, PaymentCodes.getById(10));
   }

   @Test
   void shouldThrowForUnknownPaymentCode() {
      final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> PaymentCodes.getById(999));
      assertTrue(ex.getMessage().contains("999"));
   }

   @Test
   void shouldRenderCsvForPaymentCodeWithSingleSkonto() {
      assertEquals("60;30;02,00;;;1", PaymentCodes.PC10.getCsvString());
   }

   @Test
   void shouldRenderCsvForPaymentCodeWithTwoSkontoValues() {
      assertEquals(";14;03,00;14;03,00;F", PaymentCodes.PC30.getCsvString());
   }
}
