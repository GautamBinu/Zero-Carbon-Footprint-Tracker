@echo off
REM GreenPrint GUI - Compilation and Execution Script for Windows
REM This script compiles and runs the GreenPrint GUI application

echo ========================================
echo   GreenPrint GUI - Setup ^& Launch
echo ========================================

REM Set JavaFX SDK path - adjust this path to your installation
set JAVAFX_PATH=C:\Program Files\javafx-sdk\lib

echo JavaFX SDK Path: %JAVAFX_PATH%

REM Check if JavaFX SDK exists
if not exist "%JAVAFX_PATH%" (
    echo Error: JavaFX SDK not found at: %JAVAFX_PATH%
    echo Please install JavaFX SDK and update the path in this script
    pause
    exit /b 1
)

echo JavaFX SDK found

REM Compile the project
echo.
echo [1/2] Compiling Java files...
javac *.java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml

if %ERRORLEVEL% NEQ 0 (
    echo Error: Compilation failed
    pause
    exit /b 1
)

echo Compilation successful

REM Run the application
echo.
echo [2/2] Launching GreenPrint GUI...
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml GreenPrintGUI

echo.
echo ========================================
echo Application closed
echo ========================================
pause
