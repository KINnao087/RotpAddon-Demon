@echo off 
setlocal enabledelayedexpansion 
set /a t=0 
for /f \"tokens=2 delims=:\" %%%%a in ('git grep -I -c . -- *.java') do set /a t+=%%%%a 
echo !t! 
