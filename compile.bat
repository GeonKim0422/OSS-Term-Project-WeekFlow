@echo off
rem Compile Java source files into the out directory

set "SRC=src\main\java"
set "OUT=out"

if not exist "%OUT%" mkdir "%OUT%"

rem Compile all .java files under the com.weekflow package
javac -d "%OUT%" %SRC%\com\weekflow\*.java

echo Compilation completed. Classes are in the "%OUT%" folder.
