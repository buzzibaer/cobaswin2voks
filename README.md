# cobaswin2voks

Legacy CobasWin export and synchronization utility for Voks and Agenda.

This repository has been modernized from an Eclipse-only Java 6 project to a Maven-based Java 25 project while preserving legacy business behavior.

## Modernization summary

- Build migrated to Maven (`pom.xml`), including dependency and plugin management.
- Compiler target upgraded to Java 25 (`maven.compiler.release=25`).
- Eclipse metadata removed (`.classpath`, `.project`, `.settings`).
- JDBC startup made configurable to support modern JDKs without `sun.jdbc.odbc.JdbcOdbcDriver`.
- `finalize()` cleanup removed from database classes (modern Java compatibility).
- Unit test suite added with JUnit 5.
- Coverage reporting added with JaCoCo 0.8.14.

## Technical architecture

### High-level modules

- `ctvdkip.CTVDKIP`: application entrypoint and mode dispatcher.
- `ctvdkip.business.Voks`: orchestration for CobasWin-to-Voks synchronization.
- `ctvdkip.business.CobasWin`: split and migration helper logic.
- `ctvdkip.database.cobaswin.CobasWinDB`: CobasWin JDBC access layer.
- `ctvdkip.database.voks.VoksDB`: Voks JDBC access layer.
- `ctvdkip.database.voks.AccountingRecordWriter`: writes legacy Voks import text files.
- `ctvdkip.database.agenda.AgendaRecordWriter`: writes Agenda CSV export files.
- `ctvdkip.util.ApplicationLogger`: file logger (`Application.log`).

### Runtime modes

The first CLI argument selects execution mode:

- `CobasWinToVoks`: syncs debitors/kreditors from CobasWin into Voks.
- `GenerateAccountingRecordsFromCobasWin`: writes Voks accounting import file and marks exported CobasWin records as processed.
- `GenerateAgendaAccountingRecordsFromCobasWin`: writes Agenda accounting CSV and marks exported CobasWin records as processed.
- `GenerateAgendaPersonAccountsFromCobasWin`: writes Agenda person accounts CSV (debitors + kreditors).

Optional second argument (any value) enables account number migration for modes that support it:

- `7 -> 6` account migration for person account numbers.
- `7 -> 6` migration for accounting records in Voks export mode.

Agenda accounting export always applies `4 -> 6` account migration for account fields.

## Build requirements

- JDK 25 (LTS)
- Maven 3.9+

Verify toolchain:

```bash
java -version
javac -version
mvn -version
```

## Build, test, coverage

Run unit tests:

```bash
mvn test
```

Run full verification and generate JaCoCo report:

```bash
mvn clean verify
```

JaCoCo report output:

- `target/site/jacoco/index.html`
- `target/site/jacoco/jacoco.xml`

## Running the application

Build jar:

```bash
mvn -DskipTests package
```

Run examples:

```bash
java -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar ctvdkip.CTVDKIP CobasWinToVoks
java -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar ctvdkip.CTVDKIP GenerateAccountingRecordsFromCobasWin
java -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar ctvdkip.CTVDKIP GenerateAgendaAccountingRecordsFromCobasWin
java -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar ctvdkip.CTVDKIP GenerateAgendaPersonAccountsFromCobasWin
```

Enable optional 7->6 migration by adding a second argument:

```bash
java -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar ctvdkip.CTVDKIP CobasWinToVoks migrate
```

## JDBC/ODBC connectivity on modern Java

The JDK-internal bridge `sun.jdbc.odbc.JdbcOdbcDriver` is not available on modern Java versions.

Use an external JDBC-ODBC bridge and configure this application via JVM properties or environment variables.

Configuration resolution order:

1. JVM property
2. Environment variable
3. Built-in default

### CobasWin connection settings

- `ctvdkip.cobaswin.jdbc.driver` or `CTV_COBASWIN_JDBC_DRIVER` (default: empty)
- `ctvdkip.cobaswin.jdbc.url` or `CTV_COBASWIN_JDBC_URL` (default: `jdbc:odbc:cobaswin`)
- `ctvdkip.cobaswin.jdbc.user` or `CTV_COBASWIN_JDBC_USER` (default: `dataflex`)
- `ctvdkip.cobaswin.jdbc.password` or `CTV_COBASWIN_JDBC_PASSWORD` (default: `dataflex`)

### Voks connection settings

- `ctvdkip.voks.jdbc.driver` or `CTV_VOKS_JDBC_DRIVER` (default: empty)
- `ctvdkip.voks.jdbc.url` or `CTV_VOKS_JDBC_URL` (default: `jdbc:odbc:VoksDB`)
- `ctvdkip.voks.jdbc.user` or `CTV_VOKS_JDBC_USER` (default: empty)
- `ctvdkip.voks.jdbc.password` or `CTV_VOKS_JDBC_PASSWORD` (default: empty)

Example with explicit drivers:

```bash
java \
  -Dctvdkip.cobaswin.jdbc.driver=com.example.odbc.Driver \
  -Dctvdkip.voks.jdbc.driver=com.example.odbc.Driver \
  -cp target/cobaswin2voks-1.0.0-SNAPSHOT.jar \
  ctvdkip.CTVDKIP GenerateAccountingRecordsFromCobasWin
```

## Output artifacts

- `Buchungsstapel_yyyyMMddkkmmss.txt`: Voks accounting import format.
- `Agenda_Buchungsstapel_OP_yyyyMMddkkmmss.csv`: Agenda accounting export.
- `Agenda_Personenkonten_yyyyMMddkkmmss.csv`: Agenda person account export.
- `Application.log`: application runtime log.

## Unit test scope

Unit tests intentionally focus on deterministic business logic and file formatting:

- payment code mapping and CSV rendering
- record split/update decision logic
- account number migration behavior
- accounting data normalization and formatting
- writer output structure and content

Database integration behavior is intentionally excluded from unit tests because it depends on external DSN and bridge runtime.

## Troubleshooting

- `Release version 25 not supported`: you are using a JDK older than 25.
- `No suitable driver` or `ClassNotFoundException` for driver: verify external JDBC-ODBC bridge is installed and configured.
- DSN open failures: validate DSN names (`cobaswin`, `VoksDB`) and credentials.
- If exports look incomplete, check `Application.log` for severe/warning entries before importing generated files.
