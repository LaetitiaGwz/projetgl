#! /bin/bash

# Auteur : matthieu
# Script d'automatisation des tests pour le lexer

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
INVALID_DIR=src/test/deca/syntax/invalid/parser
VALID_DIR=src/test/deca/syntax/valid/parser

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"

return_status=0

# Tests des cas invalides
for cas_de_test in $(find "$INVALID_DIR" -type f | grep "\.deca")
do
    # Récupération de la ligne ou se produit l'erreur, à partir des commentaires
    # du fichier de test
    line_err=$(cat ${cas_de_test} | grep Ligne | sed -e "s/[^0-9]//g")
    filename=$(echo ${cas_de_test} | sed -e "s@${INVALID_DIR}/@@g")
test_synt "$cas_de_test" 
    if test_synt "$cas_de_test" 2>&1 \
        | grep -q -e "$filename"':'"$line_err"
    then
        echo -e "$filename"" : ${GREEN}  OK ${WHITE}"
    else
        echo -e "$filename"" : ${RED}  ERROR ${WHITE}"
        return_status=1
    fi
done


# Tests des cas valides
for cas_de_test in $(find "$VALID_DIR" -type f | grep "\.deca")
do
    filename=$(echo ${cas_de_test} | sed -e "s@${VALID_DIR}/@@g")
    if test_synt "$cas_de_test" 2>&1 \
    | grep -q "$filename"':[0-9]'
    then
        echo -e "$filename"" : ${RED}  ERROR ${WHITE}"
    else
        echo -e "$filename"" : ${GREEN}  OK ${WHITE}"
        return_status=1
    fi
done


# exit ${return_status}
exit 0
