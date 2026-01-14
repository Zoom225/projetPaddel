@echo off
setlocal enabledelayedexpansion

REM Profil Spring : PostgreSQL (docker)
set SPRING_PROFILES_ACTIVE=docker
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=padelService
set DB_USER=padel
set DB_PASSWORD=padel

REM Supprimer les espaces en fin de ligne
set SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE: =%
set DB_HOST=%DB_HOST: =%
set DB_PORT=%DB_PORT: =%
set DB_NAME=%DB_NAME: =%
set DB_USER=%DB_USER: =%
set DB_PASSWORD=%DB_PASSWORD: =%

cd /d %~dp0
mvn spring-boot:run
