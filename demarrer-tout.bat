@echo off
echo Lancement du backend...
start "Backend" cmd /k "cd backend && mvn spring-boot:run"

echo Lancement du frontend...
start "Frontend" cmd /k "cd frontend && npm start"

echo.
echo Les deux services demarrent dans des fenetres separees.
