#! /bin/bash

# Auteur : matthieu
# Vérifications de la décompilation

cd "$(dirname "$0")"/../../.. || exit 1

# Etape C : dossiers source des tests
ETAPEC_INVALID_DIR=src/test/deca/etapeC/invalid
ETAPEC_VALID_DIR=src/test/deca/etapeC/valid
# Etape C : dossiers des résultats
ETAPEC_OUTPUT_DIR_VALID=src/test/output/etapeC/valid
ETAPEC_OUTPUT_DIR_ERROR=src/test/output/etapeC/error
ETAPEC_OUTPUT_DIR_INVALID=src/test/output/etapeC/invalid



# Etape B : dossiers source des tests
ETAPEB_VALID_DIR=src/test/deca/etapeB/valid

# Dossiers ou l'on stocke les résultats de la décompilation
DECOMPILE_DIR=src/test/output/decompile
DECOMPILE_DIR_ERROR=src/test/output/decompile/error
DECOMPILE_DIR_DECA=src/test/output/decompile/deca
DECOMPILE_DIR_DIFF=src/test/output/decompile/diff

mkdir -p "$DECOMPILE_DIR"
mkdir -p "$DECOMPILE_DIR_ERROR"
mkdir -p "$DECOMPILE_DIR_DECA"
mkdir -p "$DECOMPILE_DIR_DIFF"


GREEN="\e[1;32m"
WHITE="\e[1;37m"
RED="\e[31m"
YELLOW="\e[33m"


for cas_de_test in "$ETAPEB_VALID_DIR"/*.deca "$ETAPEC_INVALID_DIR"/*.deca "$ETAPEC_VALID_DIR"/*.deca
do
    deca_file=$(basename "$cas_de_test")
    error_file=$(basename "$cas_de_test" | sed -e "s/\.deca/\.error/")
    diff_file=$(basename "$cas_de_test" | sed -e "s/\.deca/\.diff/")
    output_file="$DECOMPILE_DIR_DECA"/"$deca_file"

    # Lancement de la compilation/décompilation
    result_test=$(decac -p "$cas_de_test" 2>&1)
    echo -e ${WHITE} "$deca_file":
    if echo "$result_test" | grep -q "UnsupportedOperationException"
    then
        echo "$result_test" > "$DECOMPILE_DIR_ERROR"/"$error_file"
        echo -e ${YELLOW}  NOT IMPLEMENTED ${WHITE}
    elif echo "$result_test" | grep -q "Exception"
    then
        echo "$result_test" > "$DECOMPILE_DIR_ERROR"/"$error_file"
        echo -e  ${RED} EXCEPTION CAUGHT ${WHITE}


    # Compilation/Decompilation sans exception
    else
        # Fichiers .deca stockés dans /decompile/deca/
        echo "$result_test" > "$output_file"

        # Verification de la sobriété de la décompilation
        if  diff "$cas_de_test" "$output_file" > "$DECOMPILE_DIR_DIFF"/"$diff_file"
        then
            echo -e ${WHITE} ${GREEN} OK ${WHITE}
        else
            echo -e ${WHITE} ${RED} WRONG DECOMPILED FILE ${WHITE}
        fi
    fi


done


exit 0


