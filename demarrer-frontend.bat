@echo off
echo ========================================
echo   Demarrage du Frontend (Angular)
echo ========================================
echo.

cd frontend

echo Verification de Node.js...
where node >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Node.js n'est pas installe
    echo Installez Node.js depuis https://nodejs.org/
    pause
    exit /b 1
)

echo.
echo Installation des dependances npm...
if not exist "node_modules" (
    call npm install --legacy-peer-deps
    if %ERRORLEVEL% NEQ 0 (
        echo ERREUR: Echec de l'installation npm
        echo.
        echo Tentative avec --force...
        call npm install --force
        if %ERRORLEVEL% NEQ 0 (
            echo ERREUR: Echec de l'installation npm
            pause
            exit /b 1
        )
    )
)

echo.
echo Demarrage du serveur Angular...
echo Le frontend sera accessible sur http://localhost:4200
echo.
echo Appuyez sur Ctrl+C pour arreter le serveur
echo.

call npm start

pause

