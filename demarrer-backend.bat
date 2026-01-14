@echo off
echo ========================================
echo   Demarrage du Backend (Spring Boot)
echo ========================================
echo.

cd backend

REM Profil Spring : PostgreSQL (docker)
set SPRING_PROFILES_ACTIVE=docker
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=padelService
set DB_USER=padel
set DB_PASSWORD=padel

REM Supprimer les espaces en fin de ligne
set SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE: =%
set DB_HOST=%DB_HOST: =%
set DB_PORT=%DB_PORT: =%
set DB_NAME=%DB_NAME: =%
set DB_USER=%DB_USER: =%
set DB_PASSWORD=%DB_PASSWORD: =%

REM Vérifier si Maven est installé
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Maven n'est pas installe ou n'est pas dans le PATH.
    echo.
    echo Solutions:
    echo 1. Installez Maven depuis https://maven.apache.org/download.cgi
    echo 2. Ajoutez Maven au PATH systeme
    echo 3. Redemarrez ce terminal apres l'installation
    echo.
    pause
    exit /b 1
)

echo Installation des dependances Maven...
call mvn clean install -q

if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Echec de l'installation Maven
    pause
    exit /b 1
)

echo.
echo Demarrage du serveur Spring Boot...
echo Le backend sera accessible sur http://localhost:8080
echo.
echo Appuyez sur Ctrl+C pour arreter le serveur
echo.

call mvn spring-boot:run

pause

