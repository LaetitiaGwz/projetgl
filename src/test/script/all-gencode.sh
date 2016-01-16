#! /bin/bash

# Auteur : matthieu
# Script d'automatisation des tests pour la generation de code

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
INVALID_DIR=src/test/deca/etapeC/invalid
VALID_DIR=src/test/deca/etapeC/valid
OUTPUT_DIR_VALID=src/test/output/etapeC/valid
OUTPUT_DIR_ERROR=src/test/output/etapeC/error
OUTPUT_DIR_INVALID=src/test/output/etapeC/invalid


ETAPEB_INVALID_DIR=src/test/deca/etapeB/invalid
ETAPEB_VALID_DIR=src/test/deca/etapeB/valid
ETAPEB_OUTPUT_DIR=src/test/output/etapeB

GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"
YELLOW="\e[33m"

return_status=0

success=0
fail=0

echo -e "${WHITE} ------------- TESTS DE LA PARTIE B (COMPILATION SEULEMENT) ----------------------"

#######################################################
# Test des cas de la partie B sur le compilateur complet
# On teste ici seulement la compilation (donc la génération de code),
# pas l'éxécution
#######################################################
for cas_de_test in "$ETAPEB_VALID_DIR"/*.deca
do
        line_err=$(cat ${cas_de_test} | grep Ligne | sed -e "s/[^0-9]//g")
        filename=$(echo ${cas_de_test} | xargs basename)
        result_test=$(decac "$cas_de_test" 2>&1)

        if [[ "$result_test" == "" ]];
        then
                echo -e "$filename"" : ${GREEN}  OK ${WHITE}"

                # On rassemble les fichiers .ass dans src/test/output/
                assembly_file=$(echo "$filename" | sed -e "s/.deca/.ass/")
                assembly_dir_new="$ETAPEB_OUTPUT_DIR""/valid/""$assembly_file"
                assembly_dir_old=$(echo "$cas_de_test" | sed -e "s@\.deca@\.ass@")
                mv "$assembly_dir_old" "$assembly_dir_new"
                success=$(($success + 1))
        else
                echo -e "$filename"" : ${RED}  ERROR ${WHITE}"

                # Ecriture d'un fichier .error
                error_file=$(echo "$filename" | sed -e "s/.deca/.error/g")
                echo "$result_test" > "$ETAPEB_OUTPUT_DIR""/error/""$error_file"
                fail=$(($fail + 1))
        fi
done

echo -e "${WHITE} ------------- TESTS DE LA PARTIE C : COMPILATION + EXECUTION ----------------------"
#######################################################
# Test des cas de la partie C
#######################################################

# Test des cas invalides (erreur à l'éxécution)
for cas_de_test in "$INVALID_DIR"/*.deca
do
        # Récupération de la ligne ou se produit l'erreur, à partir des commentaires
        # du fichier de test
        filename=$(echo ${cas_de_test} | sed -e "s@${INVALID_DIR}/@@g")
        result_test=$(decac "$cas_de_test" 2>&1)

        ######### TESTS QUI NE COMPILENT PAS ################
        if echo "$result_test" | grep -q "UnsupportedOperationException"
        then
                echo -e "$filename"" : ${YELLOW}  NOT IMPLEMENTED ${WHITE}"
                fail=$(($fail + 1))
        elif echo "$result_test" | grep -q "Exception"
        then
                # Ecriture du résultat dans un fichier .error
                error_file=$(echo "$filename" | sed -e "s/.deca/.error/g")
                echo "$result_test" > "$OUTPUT_DIR_ERROR"/"$error_file"

                echo -e "$filename"" : ${RED} EXCEPTION CAUGHT ${WHITE}"
                fail=$(($fail + 1))
                return_status=1


        ######### TESTS QUI COMPILENT ################
        else
                # On rassemble les fichiers .ass dans src/test/output/
                assembly_file=$(echo "$filename" | sed -e "s/.deca/.ass/g")
                assembly_dir="$OUTPUT_DIR_INVALID"'/'"$assembly_file"
                mv "$INVALID_DIR"/"$assembly_file" "$assembly_dir"

                echo -e "$filename"
                echo -e "Compilation : ${GREEN}  OK ${WHITE}"

                expected_output=$(cat "$cas_de_test" | grep "ima_output:" |sed -e  "s@//@@g" | sed -e "s@ima_output:@@;s@ @@g;s@ima_output:@@")
                real_output=$(ima "$assembly_dir")
                return_value=$?

                # Vérifie deux choses : 1) le code d'erreur est bon 2) ima retourne bien un nombre != 0
                if [[ "$expected_output" == $(echo "$real_output" | grep -o "$expected_output")  &&  "$return_value" != 0 ]];
                then
                    echo -e "Execution : ${GREEN}  OK ${WHITE}"
                    success=$(($success + 1))
                else
                    echo -e "Execution : ${RED}  ERROR ${WHITE}"
                    echo -e "Got output : "$real_output"; but expected output : "$expected_output" ."
                    fail=$(($fail + 1))
                fi
        fi

        echo -e "\n"
done



# Test des cas valides
for cas_de_test in "$VALID_DIR"/*.deca
do
        filename=$(echo ${cas_de_test} | sed -e "s@${VALID_DIR}/@@g")
        result_test=$(decac "$cas_de_test" 2>&1)

        ######### TESTS QUI NE COMPILENT PAS ################
        if echo "$result_test" | grep -q "UnsupportedOperationException"
        then
                echo -e "$filename"" : ${YELLOW}  NOT IMPLEMENTED ${WHITE}"
                fail=$(($fail + 1))
        elif echo "$result_test" | grep -q "Exception"
        then
                # Ecriture du résultat dans un fichier .error
                error_file=$(echo "$filename" | sed -e "s/.deca/.error/g")
                echo "$result_test" > "$OUTPUT_DIR_ERROR"/"$error_file"

                echo -e "$filename"" : ${RED} EXCEPTION CAUGHT ${WHITE}"
                fail=$(($fail + 1))


        #########TESTS QUI COMPILENT################
        else
                # On rassemble les fichiers .ass dans src/test/output/
                assembly_file=$(echo "$filename" | sed -e "s/.deca/.ass/g")
                assembly_dir="$OUTPUT_DIR_VALID"'/'"$assembly_file"
                mv "$VALID_DIR"/"$assembly_file" "$assembly_dir"

                echo -e "$filename"
                echo -e "Compilation : ${GREEN}  OK ${WHITE}"

                expected_output=$(cat "$cas_de_test" | grep "ima_output:" |sed -e  "s@//@@g" | sed -e "s@ima_output:@@;s@ @@g;s@ima_output:@@")
                real_output=$(ima "$assembly_dir" )
                if [[ "$expected_output" == "$real_output" ]];
                then
                    echo -e "Execution : ${GREEN}  OK ${WHITE}"
                    success=$(($success + 1))
                else
                    echo -e "Execution : ${RED}  ERROR ${WHITE}"
                    echo -e "Got output : "$real_output", but expected output : "$expected_output" "
                    fail=$(($fail + 1))
                fi
        fi

        echo -e "\n"
done

echo "Test réussis : $success"
echo "Test failed : $fail"

exit "$fail"

