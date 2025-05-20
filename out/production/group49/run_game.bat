@echo off

REM Set the source directory
set SRC_DIR=src

REM Set the output directory for compiled classes
set OUT_DIR=out/production/group49

REM Create the output directory if it doesn't exist
if not exist "%OUT_DIR%" (
    mkdir "%OUT_DIR%"
)

REM Compile the Java files
echo Compiling Java files...

REM Check if the compilation was successful
if errorlevel 1 (
    echo Compilation failed. Exiting...
    pause
    exit /b 1
)

REM Run the game
echo Starting the game...
java -cp "%OUT_DIR%" src.Play

REM Pause the terminal to keep it open after execution
pause