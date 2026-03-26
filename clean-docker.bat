@echo off
echo ========================================
echo Nettoyage complet de Docker
echo ========================================
echo.
echo ATTENTION: Cela va supprimer tous les conteneurs et volumes!
echo.
pause
echo.
echo 1. Arrêt des conteneurs...
docker-compose down -v
echo.
echo 2. Suppression du volume de donnees...
docker volume rm padelservice-projet-2_padelservice_data 2>nul
echo.
echo 3. Suppression des images (optionnel)...
REM Decommentez la ligne suivante pour supprimer aussi l'image
REM docker rmi padelservice-projet-2-backend:latest 2>nul
echo.
echo ========================================
echo Nettoyage termine!
echo ========================================
echo.
echo Vous pouvez maintenant relancer avec: start-docker.bat
echo.
