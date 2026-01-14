@echo off
echo ========================================
echo   Demarrage de la Base PostgreSQL
echo ========================================
echo.

where docker >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo ERREUR: Docker n'est pas installe ou n'est pas dans le PATH.
  echo Installez Docker Desktop: https://www.docker.com/products/docker-desktop/
  echo.
  pause
  exit /b 1
)

echo Lancement de PostgreSQL (docker-compose)...
docker compose up -d

if %ERRORLEVEL% NEQ 0 (
  echo ERREUR: Docker compose a echoue.
  pause
  exit /b 1
)

echo.
echo OK. PostgreSQL est lance sur localhost:5432 (db=padelService, user=padel, pwd=padel)
echo.
pause
