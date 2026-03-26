@echo off
REM Script de reinitialisation complete avec PostgreSQL
echo ========================================
echo Reinitialisation complete de PostgreSQL
echo ========================================
echo.
echo Cette action va:
echo - Arreter tous les conteneurs
echo - Supprimer le volume de donnees
echo - Reconstruire l'image backend
echo - Redemarrer tout avec PostgreSQL propre
echo.
pause
echo.
echo Etape 1: Arrêt des conteneurs...
docker-compose down -v
echo.
echo Etape 2: Suppression du volume (pour forcer la reinit)...
docker volume rm padelservice-projet-2_padelservice_data 2>nul
docker volume prune -f 2>nul
echo.
echo Etape 3: Reconstruction de l'image backend (cela peut prendre quelques minutes)...
docker-compose build --no-cache
echo.
echo Etape 4: Demarrage des conteneurs...
docker-compose up -d
echo.
echo Etape 5: Attente du demarrage de PostgreSQL (15 secondes)...
timeout /t 15
echo.
echo Etape 6: Verification de l'etat...
docker ps
echo.
echo ========================================
echo Reinitialisation terminee!
echo ========================================
echo.
echo Pour consulter les logs:
echo   logs-backend.bat
echo   docker logs padelservice-db
echo.
echo Informations de connexion PostgreSQL:
echo - Host: localhost
echo - Port: 5432
echo - Username: padel
echo - Password: padel
echo - Database: padelService
echo.
