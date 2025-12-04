@echo off
echo Starting Inventory System...
call mvnw.cmd javafx:run
if %errorlevel% neq 0 (
    echo.
    echo An error occurred while running the application.
    pause
)
