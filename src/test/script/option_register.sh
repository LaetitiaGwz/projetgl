#! /bin/bash

# Auteur : matthieu
# Script permettant de tester le bon fonctionnement de l'option -r
# Le script s'arrete dès la première détection erreur (ie un mauvais registre est utilisé
# dans le code assembleur )

# Retourne : 0 si tout les fichiers passent le test, 1 sinon

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
TESTS_REG_DIR=src/test/deca/codegen/valid/register
ETAPEC_VALID_DIR=src/test/deca/codegen/valid

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"

fail=0
success=0

for cas_de_test in $(find "$ETAPEC_VALID_DIR" -type f | grep "\.deca")
do
    filename=$(echo ${cas_de_test} | xargs basename)
    # Test avec différentes valeurs de -r
    for nb_max_reg in 4 5 6 7 8
    do
        result=$(decac -r "$nb_max_reg" "$cas_de_test" 2>/dev/null)

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
                    echo -e " ${RED} Fichier "$filename" utilise le registre "$reg_string" avec l'option -r "$nb_max_reg" "
                    fail=$(($fail + 1))
                fi
            done

            # Vérification du résultat
            expected_output=$(cat "$cas_de_test" | grep "ima_output:" |sed -e  "s@//@@g" | sed -e "s@ima_output:@@;s@ @@g;s@ima_output:@@")
            assembly_file=$(echo "$cas_de_test" | sed -e "s/\.deca/\.ass/")
            real_output=$(ima "$assembly_file" | sed -e "s@ @@g;")
            return_value=$?

            if [[ "$return_value" == 0 && "$expected_output" == $(echo "$real_output" | grep -o -- "$expected_output") ]];
                then
                    echo -e "decac -r "$nb_max_reg" "$filename" ${GREEN}  OK" "${WHITE}"
                    success=$(($success + 1))
                else
                    echo -e "$decac -r "$nb_max_reg" "$filename" ${RED} EXECUTION ERROR "${WHITE}" "
                    echo -e "Got output : "$real_output"; but expected output : "$expected_output" ."
                    fail=$(($fail + 1))
                    break;
                fi
        fi
    done
done

echo "Test réussis : $success"
echo "Test failed : $fail"
exit $fail


