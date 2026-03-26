@echo off
echo ========================================
echo Demarrage de l'application Padel Service
echo ========================================
echo.
echo 1. Arrêt des conteneurs existants...
docker-compose down
echo.
echo 2. Suppression de l'ancien volume de donnees...
docker volume rm padelservice-projet-2_padelservice_data 2>nul
echo.
echo 3. Reconstruction de l'image backend...
docker-compose build --no-cache
echo.
echo 3. Demarrage des conteneurs...
docker-compose up -d
echo.
echo 4. Attente du demarrage de la base de donnees (10 secondes)...
timeout /t 10
echo.
echo 5. Verification de l'etat des conteneurs...
docker ps
echo.
echo ========================================
echo Application demarree !
echo ========================================
echo.
echo Acces :
echo - Backend API : http://localhost:8080
echo - Base de donnees (pgAdmin) : localhost:5432
echo   - Username : padel
echo   - Password : padel
echo   - Database : padelService
echo.
