@echo off
echo Compiling JavaFX project...
javac --module-path javafx-sdk-21\lib --add-modules javafx.controls -cp lib\jSerialComm-2.9.3.jar -d out src\*.java
echo Compilation finished.
pause