@echo off
REM ========================================
REM   Padel Management System - Quick Stop
REM   Arrête tous les services
REM ========================================

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║  🛑 Padel Management System - Arrêt des Services           ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

echo [1/3] Arrêt de PostgreSQL en Docker...
docker compose down
echo ✅ PostgreSQL arrêté

echo.
echo [2/3] Les services Frontend et Backend vont s'arrêter automatiquement
echo Fermez les fenêtres PowerShell si elles sont encore ouvertes
echo.

echo [3/3] Nettoyage...
echo ✅ Les services sont arrêtés

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║  ✅ Tous les services ont été arrêtés                      ║
echo ║                                                              ║
echo ║  Pour redémarrer:                                            ║
echo ║  .\demarrer-complet.ps1                                     ║
echo ║                                                              ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

pause
