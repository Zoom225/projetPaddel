@echo off
setlocal enabledelayedexpansion
echo ========================================
echo   Demarrage de l'Application Complete
echo ========================================
echo.
echo Ce script va lancer le backend et le frontend
echo dans des fenetres separees.
echo.
echo Verification des prerequis en cours...
echo.

set MISSING_TOOLS=0

REM Vérifier Docker (pour PostgreSQL)
echo [1/4] Verification de Docker...
where docker >nul 2>&1
if errorlevel 1 (
    echo        [X] Docker NON TROUVE
    set MISSING_TOOLS=1
) else (
    echo        [OK] Docker installe
)

REM Vérifier Java (pour le backend)
echo [2/4] Verification de Java...
where java >nul 2>&1
if errorlevel 1 (
    echo        [X] Java NON TROUVE
    set MISSING_TOOLS=1
) else (
    echo        [OK] Java installe
)

REM Vérifier Maven (pour le backend)
echo [3/4] Verification de Maven...
where mvn >nul 2>&1
if errorlevel 1 (
    echo        [X] Maven NON TROUVE
    set MISSING_TOOLS=1
) else (
    echo        [OK] Maven installe
)

REM Vérifier Node.js (pour le frontend)
echo [4/4] Verification de Node.js...
where node >nul 2>&1
if errorlevel 1 (
    echo        [X] Node.js NON TROUVE
    set MISSING_TOOLS=1
) else (
    echo        [OK] Node.js installe
)

echo.
REM Vérifier si tous les outils sont présents
where docker >nul 2>&1
if errorlevel 1 goto :missing_tools
where java >nul 2>&1
if errorlevel 1 goto :missing_tools
where mvn >nul 2>&1
if errorlevel 1 goto :missing_tools
where node >nul 2>&1
if errorlevel 1 goto :missing_tools

REM Tous les outils sont présents
echo ========================================
echo   Tous les prerequis sont OK !
echo ========================================
goto :start_app

:missing_tools
echo ========================================
echo   ERREUR: Outils manquants detectes
echo ========================================
echo.
echo Veuillez installer les outils manquants.
echo Consultez DEMARRAGE_RAPIDE.md pour les instructions.
echo.
echo ========================================
echo.
echo Apres installation, REDEMARREZ votre terminal
echo et relancez ce script.
echo.
pause
exit /b 1

:start_app
echo.
echo Appuyez sur une touche pour lancer l'application...
pause >nul

echo.
echo Demarrage de la base PostgreSQL...
REM Arrêter et supprimer le conteneur existant s'il existe
docker stop padelservice-db >nul 2>&1
docker rm padelservice-db >nul 2>&1
cd /d %~dp0
start "DB - PostgreSQL" cmd /k "cd /d %~dp0 && docker compose up -d"
cd backend

timeout /t 4 /nobreak >nul

echo.
echo Demarrage du Backend...
cd backend
call mvn clean install -q >nul 2>&1
start "Backend - Spring Boot" cmd /k "cd /d %~dp0backend && call start-backend-docker.bat"

timeout /t 8 /nobreak >nul

echo.
echo Demarrage du Frontend...
cd ..\frontend
if not exist "node_modules" (
    echo Installation des dependances npm...
    call npm install --legacy-peer-deps >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo Tentative avec --force...
        call npm install --force >nul 2>&1
    )
)
start "Frontend - Angular" cmd /k "cd /d %~dp0frontend && npm start"

cd ..

echo.
echo ========================================
echo   Application lancee !
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:4200
echo.
echo Les donnees d'exemple seront creees automatiquement
echo au premier demarrage du backend.
echo.
echo Fermez les fenetres pour arreter les serveurs
echo.
pause

