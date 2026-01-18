# RideWise (Console, Java 25)

RideWise is a small, console-based ride-sharing simulation built to demonstrate **LLD + SOLID + Strategy pattern**. Data is **in-memory** (no DB, no network).

## What you can do
- Register riders and drivers
- List available drivers
- Request/complete/cancel rides
- Swap matching and pricing algorithms via strategies

## Project layout
- **app**: `src/main/java/com/airtribe/ridewise`
- **tests** (custom runner): `src/test/java/com/airtribe/ridewise/tests`
- **docs**: `docs/`

## Run (Windows)

### Option A: Maven (`pom.xml`)
Prereq: Maven installed and `mvn` available on PATH.

```bat
mvn -q clean compile exec:java
```

### Option B: Plain `javac`/`java`

```bat
.\run.bat
```

Non-interactive smoke run (prints menu once, exits):

```bat
cmd /c "echo 8| run.bat"
```

## Test (Windows)
This repo uses a lightweight custom test runner (no JUnit dependency).

```bat
.\test.bat
```

## Extension points
- **Driver matching**: implement `com.airtribe.ridewise.strategy.RideMatchingStrategy`
- **Fare calculation**: implement `com.airtribe.ridewise.strategy.FareStrategy`

## Docs (recommended reading order)
- `docs/Requirements.md`: scope, assumptions, and MVP requirements
- `docs/Class_Model.md`: key classes, responsibilities, and invariants
- `docs/Object_Relationships.md`: relationships + lifecycle view
- `docs/SOLID_Reflection.md`: where SOLID shows up in this codebase
