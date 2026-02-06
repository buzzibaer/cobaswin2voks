package ctvdkip.database.voks;

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

class AccountingRecordWriterTest {

   private final List<Path> createdFiles = new ArrayList<Path>();

   @AfterEach
   void cleanupGeneratedFiles() throws IOException {
      for (final Path file : createdFiles) {
         Files.deleteIfExists(file);
      }
   }

   @Test
   void shouldWriteCobasWinAccountingRecordFile() throws IOException {
      final Set<String> filesBefore = listMatchingFileNames("Buchungsstapel_", ".txt");

      final AccountingRecord record = new AccountingRecord();
      record.setBelegDatum("01.01.2024");
      record.setBelegNummer("B1");
      record.setBelegText("Text");
      record.setKonto("1234");
      record.setGegenKonto("5678");
      record.setBetrag("10050");
      record.setUstSchluessel("19");
      record.setSkonto("0");
      record.setKostenstelle("K1");
      record.setKosteenstelleVomKonto("K2");

      final AccountingRecordWriter writer = new AccountingRecordWriter();
      assertTrue(writer.writeCobasWinAccountingRecordsToFile(Collections.singletonList(record)));

      final Path file = findNewFile("Buchungsstapel_", ".txt", filesBefore);
      createdFiles.add(file);
      final List<String> lines = Files.readAllLines(file);

      assertEquals(2, lines.size());
      assertTrue(lines.get(0).matches("\\$iks\\$#\\d{2}\\.\\d{2}\\.\\d{4}#FIBU#FIBU"));
      assertEquals("01.01.2024:B1:Text:1234:5678:10050:19:0:K1:K2::::::1", lines.get(1));
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
