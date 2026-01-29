@echo off
echo Lancement de l'assistant d'installation Pharmacie Manager...

:: Detection des dossiers de compilation (NetBeans vs Manual)
:: Always compile from src to ensure latest changes are used
if not exist "bin" mkdir "bin"
echo Compilation des sources (Mise a jour)...
javac -d bin -cp "libs/*" -source 1.8 -target 1.8 -sourcepath src src/com/pharmacie/model/*.java src/com/pharmacie/dao/*.java src/com/pharmacie/view/*.java src/com/pharmacie/setup/*.java > compile.log 2>&1
set CLASSPATH="bin;libs/*"

start "" javaw -cp %CLASSPATH% com.pharmacie.setup.SetupFrame
exit
