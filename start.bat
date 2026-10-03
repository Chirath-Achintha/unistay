@echo off
title UniStay Launcher
color 0A

echo.
echo  ===================================================
echo    UniStay - Full Stack Launcher
echo  ===================================================
echo.

echo  [1/3] Starting Backend (Spring Boot on :8080) ...
start "UniStay Backend" cmd /k "%~dp0backend\start-backend.bat"

echo  [2/3] Starting Frontend (Python HTTP server on :5500) ...
start "UniStay Frontend" cmd /k "%~dp0frontend\start-frontend.bat"

echo  [3/3] Waiting 10 seconds then opening browser...
timeout /t 10 /nobreak >nul

start "" "http://localhost:5500"

echo.
echo  ===================================================
echo    Both services started!
echo.
echo    Frontend  ->  http://localhost:5500
echo    Backend   ->  http://localhost:8080/api
echo    Health    ->  http://localhost:8080/api/health
echo  ===================================================
echo.
echo  Close the two terminal windows to stop the servers.
echo.
pause
