@echo off
echo ========================================
echo Etat des conteneurs Docker
echo ========================================
echo.
docker ps -a
echo.
echo ========================================
echo Logs de la base de donnees :
echo ========================================
docker logs padelservice-db
echo.
echo ========================================
echo Logs du backend :
echo ========================================
docker logs padelservice-backend
