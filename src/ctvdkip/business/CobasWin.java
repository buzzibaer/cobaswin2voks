package ctvdkip.business;

import java.util.LinkedList;
import java.util.List;

import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.VoksRecord;
import ctvdkip.util.ApplicationLogger;

/**
 * Created by IntelliJ IDEA. User: rbust Date: 23.08.2004 Time: 23:27:35 To change this template use File | Settings |
 * File Templates.
 */
public class CobasWin {

   public CobasWin() {
   }

   /**
    * Method is splitting the given LinkedList of Debitors/Kreditors from CobasWin into NEW and UPDATE Debitors
    *
    * @param p_elementstosplitt the List with Kreditors or Debitors to splitt (Elements in List = VoksRecord)
    * @param p_compareelements the List with all Kreditors/Debitors from CobasWin for comparison
    * @return LinkedList[] 0 = New Debitors/Kreditors (to insert) 1 = Update Debitors/Kreditors (for update)
    */
   public <E extends VoksRecord> List<List<E>> splitIntoUpdateAndInsert(final List<E> p_elementstosplitt, final List<E> p_compareelements) {

      // local variables
      final LinkedList<E> _new_elements = new LinkedList<E>();
      final LinkedList<E> _update_elements = new LinkedList<E>();
      final LinkedList<List<E>> tmpList = new LinkedList<List<E>>();

      tmpList.add(_new_elements);
      tmpList.add(_update_elements);

      for (final E _tmprecord : p_elementstosplitt) {

         boolean _found = false;

         if (_tmprecord.getKundenNr().equalsIgnoreCase("")) {

            // Kreditor detected
            for (final E _comparerecord : p_compareelements) {

               if (_tmprecord.getLieferantenNr().equalsIgnoreCase(_comparerecord.getLieferantenNr())) {

                  //record found in List adding to
                  //adding to update list
                  _update_elements.add(_tmprecord);

                  _found = true;
                  break;
               }

            }

            if (_found == true) {
               //found and added to update list
               //doining nothing
               _found = false;
            } else {
               //element not found, must be NEW
               //adding to new element list
               _new_elements.add(_tmprecord);
            }

         } else {

            //Debitor detected
            for (final E _comparerecord : p_compareelements) {

               if (_tmprecord.getKundenNr().equalsIgnoreCase(_comparerecord.getKundenNr())) {

                  //record found in List adding to
                  //adding to update list
                  _update_elements.add(_tmprecord);

                  _found = true;
                  break;
               }

            }

            if (_found == true) {
               //found and added to update list
               //doining nothing
               _found = false;
            } else {
               //element not found, must be NEW
               //adding to new element list
               _new_elements.add(_tmprecord);
            }
         }
      }

      ApplicationLogger.getInstance().getLogger().info("SplitList for NEW Elements Size = " + _new_elements.size());
      ApplicationLogger.getInstance().getLogger().info("SplitList for UPDATE Elements Size = " + _update_elements.size());

      return tmpList;
   }

   public static void migrateAccountNumbersFrom7To6(final List<? extends VoksRecord> list) {
      for (final VoksRecord current : list) {
         if (current.getKundenNr().equalsIgnoreCase("")) {
            final String lfNr = current.getLieferantenNr();
            if (lfNr.length() == 7) {
               current.setLieferantenNr(lfNr.substring(1));
            } else {
               ApplicationLogger.getInstance().getLogger().warning("Found non 7-digit lieferantennummer " + lfNr + ", not migrating");
            }
         } else {
            final String kdNr = current.getKundenNr();
            if (kdNr.length() == 7) {
               current.setKundenNr(kdNr.substring(1));
            } else {
               ApplicationLogger.getInstance().getLogger().warning("Found non 7-digit kundennummer " + kdNr + ", not migrating");
            }
         }
      }
   }

   public static void migrateAccountRecordsFrom7to6Digits(final List<AccountingRecord> list) {
      for (final AccountingRecord current : list) {
         final String kto = current.getKonto();
         if (kto.length() == 7) {
            current.setKonto(kto.substring(1));
         } else {
            ApplicationLogger
                  .getInstance()
                  .getLogger()
                  .warning(
                        "Found non 7-digit account number " + kto + " in record " + current.getRecordNummer() + " for receipt + "
                              + current.getBelegNummer() + ", not migrating");
         }

         final String ggKto = current.getGegenKonto();
         if (ggKto.length() == 7) {
            current.setGegenKonto(ggKto.substring(1));
         } else {
            ApplicationLogger
                  .getInstance()
                  .getLogger()
                  .warning(
                        "Found non 7-digit account number " + ggKto + " in record " + current.getRecordNummer() + " for receipt + "
                              + current.getBelegNummer() + ", not migrating");
         }
      }
   }

   public static void migrateAccountRecordsFrom4To6Digits(final List<AccountingRecord> list) {
      for (final AccountingRecord current : list) {
         final String kto = current.getKonto();
         if (kto.length() == 4) {
            current.setKonto(kto.concat("00"));
         }

         final String ggKto = current.getGegenKonto();
         if (ggKto.length() == 4) {
            current.setGegenKonto(ggKto.concat("00"));
         }
      }
   }
}
