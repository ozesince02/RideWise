@echo off
setlocal enabledelayedexpansion

REM Compile and run using plain javac/java (no Maven).
REM Requires Java 25+ available on PATH (javac/java).

set "ROOT=%~dp0"
set "SRC=%ROOT%src\main\java"
set "OUT=%ROOT%out\classes"

if exist "%ROOT%out" rmdir /s /q "%ROOT%out"
mkdir "%OUT%"

REM Collect sources
set "SOURCES_FILE=%ROOT%out\sources.txt"
dir /s /b "%SRC%\*.java" > "%SOURCES_FILE%"

javac -encoding UTF-8 -d "%OUT%" @"%SOURCES_FILE%"
if errorlevel 1 (
  echo.
  echo Compilation failed.
  exit /b 1
)

java -cp "%OUT%" com.airtribe.ridewise.Main


