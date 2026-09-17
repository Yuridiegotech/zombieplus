@echo off

REM Abre o VS Code com dois terminais separados executando "npm run dev"

REM Abre o primeiro terminal com a pasta web
start "" code -n -r "C:\QAX\apps\zombieplus\web"
timeout /t 2

REM Abre o segundo terminal com a pasta api
start "" code -n -a "C:\QAX\apps\zombieplus\api"
