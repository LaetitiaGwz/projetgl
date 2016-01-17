#! /bin/bash

# Auteur : matthieu
# Script permettant de tester le bon fonctionnement de l'option -r

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
INVALID_DIR=src/test/deca/etapeA/invalid/lexer
VALID_DIR=src/test/deca/etapeA/valid/lexer

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"

return_status=0


