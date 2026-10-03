@echo off
title UniStay Frontend
color 0B

echo.
echo  =============================================
echo    UniStay Frontend - http://localhost:5500
echo  =============================================
echo.

cd /d "%~dp0"
python -m http.server 5500
