#! /bin/bash

# Auteur : matthieu
# Script permettant de tester le bon fonctionnement de l'option -r
# Le script s'arrete dès la première détection erreur (ie un mauvais registre est utilisé
# dans le code assembleur )

# Retourne : 0 si tout les fichiers passent le test, 1 sinon

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
TESTS_REG_DIR=src/test/deca/codegen/valid/register
VALID_DIR=src/test/deca/etapeA/valid/lexer

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"


for cas_de_test in "$TESTS_REG_DIR"/*.deca
do
    filename=$(echo ${cas_de_test} | xargs basename)
    # Test avec différentes valeurs de -r
    for nb_max_reg in 4 5 6 7 8
    do
        result=$(decac -r "$nb_max_reg" "$cas_de_test")
        if [ $? == 0 ]
        then
            ass_file=$(echo "$cas_de_test" | sed -e "s/\.deca/\.ass/")

            # On regarque qu'aucun registre supérieur à RMAX n'est utilisé
            for ((register=$nb_max_reg ; 15 - $register; register++))
            do
                reg_string="R""$register"
                cat "$ass_file" | grep "$reg_string"
                if [ $? == 0 ]
                then
                    echo -e " ${RED} Fichier "$cas_de_test" utilise le registre "$reg_string" avec l'option -r "$nb_max_reg" "
                    exit 1
                fi
            done
        fi
    done
done

exit 0


