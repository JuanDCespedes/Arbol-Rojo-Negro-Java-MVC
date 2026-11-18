@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\modelo\*.java src\vista\*.java src\controlador\*.java src\app\*.java src\pruebas\*.java
if errorlevel 1 (
    echo ERROR: Revisa la version de Java y la compilacion.
    pause
    exit /b 1
)
java -Dfile.encoding=UTF-8 -cp bin pruebas.PruebasArbolRojoNegro
pause
