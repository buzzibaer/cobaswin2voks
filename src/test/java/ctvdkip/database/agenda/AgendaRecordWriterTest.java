package ctvdkip.database.agenda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ctvdkip.business.PaymentCodes;
import ctvdkip.database.voks.AccountingRecord;
import ctvdkip.database.voks.VoksDebitorRecord;
import ctvdkip.database.voks.VoksKreditorRecord;

class AgendaRecordWriterTest {

   private final List<Path> createdFiles = new ArrayList<Path>();

   @AfterEach
   void cleanupGeneratedFiles() throws IOException {
      for (final Path file : createdFiles) {
         Files.deleteIfExists(file);
      }
   }

   @Test
   void shouldWriteAgendaAccountingRecordsFile() throws IOException {
      final Set<String> filesBefore = listMatchingFileNames("Agenda_Buchungsstapel_OP_", ".csv");

      final AccountingRecord record = new AccountingRecord();
      record.setBelegDatum("01.01.2024");
      record.setBelegNummer("B-1");
      record.setBelegText("Test");
      record.setKonto("1000");
      record.setGegenKonto("2000");
      record.setBetrag("1000");
      record.setUstSchluessel("19");
      record.setSkonto("0");

      final AgendaRecordWriter writer = new AgendaRecordWriter();
      assertTrue(writer.writeAgendaAccountingRecordsToFile(Collections.singletonList(record)));

      final Path file = findNewFile("Agenda_Buchungsstapel_OP_", ".csv", filesBefore);
      createdFiles.add(file);
      final List<String> lines = Files.readAllLines(file);

      assertEquals(2, lines.size());
      assertEquals("Datum;Beleg1;Buchungstext;Konto;Gegenkonto;Umsatz in Euro;Steuerschl;Skonto in Euro;Kost1;Kost2;", lines.get(0));
      assertTrue(lines.get(1).contains("01.01.2024;B-1;\"Test\";1000;2000;1000;19;0;;;"));
   }

   @Test
   void shouldWriteAgendaPersonAccountsFile() throws IOException {
      final Set<String> filesBefore = listMatchingFileNames("Agenda_Personenkonten_", ".csv");

      final VoksDebitorRecord debitor = new VoksDebitorRecord();
      debitor.setKundenNr("100001");
      debitor.setName("Debitor");
      debitor.setZusatz("Z");
      debitor.setStrasse("Street 1");
      debitor.setLand("DE");
      debitor.setPlz("12345");
      debitor.setOrt("City");
      debitor.setTelefon("123");
      debitor.setFaxNummer("456");
      debitor.setEmail("deb@example.com");
      debitor.setUstID("DE123");
      debitor.setZahlungsBedingungsCode("1");

      final VoksKreditorRecord kreditor = new VoksKreditorRecord();
      kreditor.setLieferantenNr("200001");
      kreditor.setName("Kreditor");
      kreditor.setZusatz("Z2");
      kreditor.setStrasse("Street 2");
      kreditor.setLand("DE");
      kreditor.setPlz("54321");
      kreditor.setOrt("Town");
      kreditor.setTelefon("999");
      kreditor.setFaxNummer("888");
      kreditor.setEmail("kr@example.com");

      final AgendaRecordWriter writer = new AgendaRecordWriter();
      assertTrue(writer.writeAgendaPersonAccountsToFile(Collections.singletonList(debitor), Collections.singletonList(kreditor)));

      final Path file = findNewFile("Agenda_Personenkonten_", ".csv", filesBefore);
      createdFiles.add(file);
      final List<String> lines = Files.readAllLines(file);

      assertEquals(3, lines.size());
      assertEquals("Konto;Name1;Name2;Strasse;Land;PLZ;Ort;Telefon;Telefax;E-Mail;EUID;Faellig-Netto;Faellig-1;Skonto-1;Faellig-2;Skonto-2;Zahlungsbedingung;", lines.get(0));
      assertTrue(lines.get(1).startsWith("100001;\"Debitor\";"));
      assertTrue(lines.get(1).contains(PaymentCodes.PC1.getCsvString()));
      assertTrue(lines.get(2).startsWith("200001;\"Kreditor\";"));
   }

   private Set<String> listMatchingFileNames(final String prefix, final String suffix) throws IOException {
      try (Stream<Path> files = Files.list(Path.of("."))) {
         return files
               .map(path -> path.getFileName().toString())
               .filter(name -> name.startsWith(prefix) && name.endsWith(suffix))
               .collect(Collectors.toCollection(HashSet::new));
      }
   }

   private Path findNewFile(final String prefix, final String suffix, final Set<String> filesBefore) throws IOException {
      try (Stream<Path> files = Files.list(Path.of("."))) {
         final List<Path> matches = files
               .filter(path -> {
                  final String name = path.getFileName().toString();
                  return name.startsWith(prefix) && name.endsWith(suffix) && !filesBefore.contains(name);
               })
               .collect(Collectors.toList());
         assertEquals(1, matches.size());
         return matches.get(0);
      }
   }
}
