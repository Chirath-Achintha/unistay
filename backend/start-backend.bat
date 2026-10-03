@echo off
title UniStay Backend
color 0A

echo.
echo  =============================================
echo    UniStay Backend - Spring Boot :8080
echo  =============================================
echo.

:: Kill any process holding port 8080 using PowerShell (more reliable than batch for loop)
echo  Clearing port 8080...
powershell -NoProfile -Command "Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }"
echo  Port 8080 ready.
echo.

set "DB_URL=jdbc:postgresql://ep-damp-snow-aydv5h0a-pooler.c-5.us-east-2.aws.neon.tech/neondb?sslmode=require"
set "DB_USERNAME=neondb_owner"
set "DB_PASSWORD=npg_xRM9vsgNkfr0"
set "ADMIN_EMAIL=admin@gmail.com"
set "ADMIN_PASSWORD=admin123"
set "CLOUDINARY_CLOUD_NAME=pzw11iut"
set "CLOUDINARY_API_KEY=435812181276724"
set "CLOUDINARY_API_SECRET=dOpqVo32n08XANg5b5qUk08aTJg"

echo  Starting Spring Boot...
echo.
cd /d "%~dp0"
call mvnw.cmd spring-boot:run
