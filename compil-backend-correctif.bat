@echo off
echo ========================================
echo   Compilation du Backend (Correction)
echo ========================================

REM Ajouter Maven au PATH pour cette session seulement
set "MAVEN_HOME=%USERPROFILE%\.maven\apache-maven-3.9.12"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo Verification de Maven...
call mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR : Maven n'est toujours pas trouve !
    echo Verifiez que le dossier %MAVEN_HOME% existe.
    pause
    exit /b 1
)

echo.
echo Lancement de la compilation...
cd backend
call mvn clean install

if %ERRORLEVEL% == 0 (
    echo.
    echo ========================================
    echo   SUCCES : Backend compile !
    echo ========================================
) else (
    echo.
    echo ========================================
    echo   ECHEC : La compilation a echoue.
    echo ========================================
)

pause
