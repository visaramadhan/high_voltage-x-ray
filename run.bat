@echo off
echo Running JavaFX project...
java --module-path javafx-sdk-21\lib --add-modules javafx.controls -cp out;lib\jSerialComm-2.9.3.jar Main
pause