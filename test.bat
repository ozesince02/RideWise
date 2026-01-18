@echo off
setlocal enabledelayedexpansion

REM Compile and run tests using plain javac/java (no Maven).
REM This project uses a custom test runner: com.airtribe.ridewise.tests.TestRunner
REM Requires Java 25+ available on PATH (javac/java).

set "ROOT=%~dp0"
set "SRC_MAIN=%ROOT%src\main\java"
set "SRC_TEST=%ROOT%src\test\java"
set "OUT=%ROOT%out\test-classes"

if exist "%ROOT%out" rmdir /s /q "%ROOT%out"
mkdir "%OUT%"

REM Collect sources (main + test)
set "SOURCES_FILE=%ROOT%out\sources-test.txt"
(
  dir /s /b "%SRC_MAIN%\*.java"
  dir /s /b "%SRC_TEST%\*.java"
) > "%SOURCES_FILE%"

javac -encoding UTF-8 -d "%OUT%" @"%SOURCES_FILE%"
if errorlevel 1 (
  echo.
  echo Compilation failed.
  exit /b 1
)

java -cp "%OUT%" com.airtribe.ridewise.tests.TestRunner


