#! /bin/bash

# Auteur : matthieu
# Script d'automatisation des tests pour la verification contextuelle

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
INVALID_DIR=src/test/deca/context/invalid
VALID_DIR=src/test/deca/context/valid

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"
YELLOW="\e[33m"

return_status=0

success=0
fail=0

# Tests des cas invalides
for cas_de_test in "$INVALID_DIR"/*.deca "$INVALID_DIR"/*/*.deca
do
        # Récupération de la ligne ou se produit l'erreur, à partir des commentaires
        # du fichier de test
        line_err=$(cat ${cas_de_test} | grep Ligne | sed -e "s/[^0-9]//g")
        filename=$(echo ${cas_de_test} | sed -e "s@${INVALID_DIR}/@@g")
        result_test=$(test_context "$cas_de_test" 2>&1)
        if  echo "$result_test" | grep -q "$filename"':[0-9]'
        then
                echo -e "$filename"" : ${GREEN}  OK ${WHITE}"
                success=$(($success + 1))
        elif echo "$result_test" | grep -q "UnsupportedOperationException"
        then
                echo -e "$filename"" : ${YELLOW}  NOT IMPLEMENTED ${WHITE}"
                fail=$(($fail + 1))
        elif echo "$result_test" | grep -q "Exception"
        then
                echo -e "$filename"" : ${RED} EXCEPTION CAUGHT ${WHITE}"
                fail=$(($fail + 1))
                return_status=1
    else
            echo -e "$filename"" : ${RED}  ERROR ${WHITE}"
            fail=$(($fail + 1))
            return_status=1
    fi
done

# Tests des cas valides
for cas_de_test in "$VALID_DIR"/*.deca "$VALID_DIR"/*/*.deca
do
        filename=$(echo ${cas_de_test} | sed -e "s@${VALID_DIR}/@@g")
        result_test=$(test_context "$cas_de_test" 2>&1)
        if  echo "$result_test" | grep -q "$filename"':[0-9]'
        then
                echo -e "$filename"" : ${RED}  CONTEXTUAL ERROR ${WHITE}"
                fail=$(($fail + 1))
                return_status=1
        elif echo "$result_test" | grep -q "UnsupportedOperationException"
        then
                echo -e "$filename"" : ${YELLOW}  NOT IMPLEMENTED ${WHITE}"
                fail=$(($fail + 1))
        elif echo "$result_test" | grep -q "Exception"
        then
                echo -e "$filename"" : ${RED} EXCEPTION CAUGHT ${WHITE}"
                fail=$(($fail + 1))
                return_status=1
        else
                echo -e "$filename"" : ${GREEN}  OK ${WHITE}"
                success=$(($success + 1))

    fi
done

echo "Test réussis : $success"
echo "Test failed : $fail"

exit ${return_status}
