package ctvdkip.business;

import java.text.NumberFormat;
import java.util.Locale;

public enum PaymentCodes {

   PC1(1, "14Tage3%-60Tage netto", 60, 14, 3, '1'),
   PC2(2, "14Tage2%-30Tage netto", 30, 14, 2, '1'),
   PC3(3, "sofort netto", 1, '1'),
   PC4(4, "Nachnahme", 30, '1'),
   PC5(5, "Loeschen", 'F'),
   PC6(6, "Bar", 14, '1'),
   PC7(7, "Vorauskasse", 14, '1'),
   PC9(9, "30Tage netto", 30, '1'),
   PC10(10, "30Tage2%-60Tage netto", 60, 30, 2, '1'),
   PC11(11, "14Tage3%-30Tage netto", 30, 14, 3, '1'),
   PC12(12, "30Tage3%", null, 30, 3, '2'),
   PC13(13, "30Tage2%-60Tage netto", 60, 30, 2, '1'),
   PC15(15, "60Tage netto", 60, '1'),
   PC17(17, "90Tage netto", 90, '1'),
   PC18(18, "90Tage3%", null, 90, 3, '1'),
   PC21(21, "sofort netto kasse", 1, '1'),
   PC25(25, "sofort netto 3%", null, 1, 3, '1'),
   PC30(30, "3% bei Bankeinzug", null, 14, 3, 14, 3, 'F'),
   PC31(31, "Netto Kasse bei Bankeinzug", 14, '1'),
   PC32(32, "2% bei Bankeinzug", null, 14, 2, 14, 2, 'F'),
   PC33(33, "Bankeinzug 30 Tage netto", 30, '1'),
   PC37(37, "21Tage2%-30Tage netto", 30, 21, 2, '1'),
   PC38(38, "21Tage3%-30Tage netto", 30, 21, 3, '1'),
   PC40(40, "14Tage4%-30Tage netto", 30, 14, 4, '1'),
   PC98(98, "Gutschrift mit nä Zahlung verrechnen", '1'),
   PC99(99, "Gratis", 0, 'F');

   private static final String SEPARATOR = ";";
   private static final NumberFormat PERCENT_FORMAT = NumberFormat.getInstance(Locale.GERMANY);
   static {
      PERCENT_FORMAT.setMinimumFractionDigits(2);
      PERCENT_FORMAT.setMaximumFractionDigits(2);
      PERCENT_FORMAT.setGroupingUsed(false);
      PERCENT_FORMAT.setMinimumIntegerDigits(2);
      PERCENT_FORMAT.setMaximumIntegerDigits(2);
   }

   private int id;
   private String description;
   private Integer skonto1Days;
   private Integer skonto1Percent;
   private Integer skonto2Days;
   private Integer skonto2Percent;
   private Integer nettoDays;
   private char paymentCode;

   private PaymentCodes(final int id, final String description, final char paymentCode) {
      this.id = id;
      this.description = description;
      this.paymentCode = paymentCode;
   }

   private PaymentCodes(final int id, final String description, final Integer nettoDays, final char paymentCode) {
      this(id, description, paymentCode);
      this.nettoDays = nettoDays;
   }

   private PaymentCodes(final int id, final String description, final Integer nettoDays, final Integer skonto1Days,
         final Integer skonto1Percent, final char paymentCode) {
      this(id, description, nettoDays, paymentCode);
      this.skonto1Days = skonto1Days;
      this.skonto1Percent = skonto1Percent;
   }

   private PaymentCodes(final int id, final String description, final Integer nettoDays, final Integer skonto1Days,
         final Integer skonto1Percent, final Integer skonto2Days, final Integer skonto2Percent, final char paymentCode) {
      this(id, description, nettoDays, skonto1Days, skonto1Percent, paymentCode);
      this.skonto2Days = skonto2Days;
      this.skonto2Percent = skonto2Percent;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public String getCsvString() {
      /*
       * Structure: Faellig-Netto 2 n/K Fälligkeitstage Faellig-1 2 n/K Fälligkeitstage Skontofrist 1 Skonto-1 5 n/K
       * 99,99 Skonto in Prozent für Skontofrist 1 Faellig-2 2 n/K Fälligkeitstage Skontofrist 2 Skonto-2 5 n/K 99,99
       * Skonto in Prozent für Skontofrist 2 Zahlungsbedingung 1 n/K »1« = Fällig netto, »2« = Fällig Skontofrist 1, »3«
       * = Fällig Skontofrist 2, »F« = Flexibel
       */
      final StringBuilder b = new StringBuilder();
      b.append(nettoDays == null ? "" : nettoDays);
      b.append(SEPARATOR);
      b.append(skonto1Days == null ? "" : skonto1Days);
      b.append(SEPARATOR);
      b.append(skonto1Percent == null ? "" : PERCENT_FORMAT.format(skonto1Percent.longValue()));
      b.append(SEPARATOR);
      b.append(skonto2Days == null ? "" : skonto2Days);
      b.append(SEPARATOR);
      b.append(skonto2Percent == null ? "" : PERCENT_FORMAT.format(skonto2Percent.longValue()));
      b.append(SEPARATOR);
      b.append(paymentCode);

      return b.toString();
   }

   public static PaymentCodes getById(final int id) {
      for (final PaymentCodes p : PaymentCodes.values()) {
         if (p.id == id) {
            return p;
         }
      }
      throw new IllegalArgumentException("Zahlungsbedingungscode " + id + " unbekannt!");
   }

}
