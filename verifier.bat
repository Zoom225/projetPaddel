@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo.
echo ========================================
echo   Verification de l'Application
echo ========================================
echo.

REM Vérifier Maven
echo [1/5] Verification de Maven...
mvn --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ Maven est installe
) else (
    echo ✗ Maven N'EST PAS installe
)

REM Vérifier Java
echo [2/5] Verification de Java...
java -version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ Java est installe
) else (
    echo ✗ Java N'EST PAS installe
)

REM Vérifier Node.js
echo [3/5] Verification de Node.js...
node --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ Node.js est installe
) else (
    echo ✗ Node.js N'EST PAS installe
)

REM Vérifier npm
echo [4/5] Verification de npm...
npm --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ npm est installe
) else (
    echo ✗ npm N'EST PAS installe
)

REM Vérifier Docker
echo [5/5] Verification de Docker...
docker --version >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✓ Docker est installe
) else (
    echo ✗ Docker N'EST PAS installe
)

echo.
echo ========================================
echo   Verification terminee
echo ========================================
echo.
pause
