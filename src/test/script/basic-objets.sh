#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

echo "### INFO: recompiling java files for $decac ###" 
cd "$(dirname "${decac}")/../../.."
mvn compile || exit 1


if [ $1 == "clean" ]; then 
    echo "$1"
    rm ${root}/codegen/valid/Objets/*.ass
    rm ${root}/codegen/valid/Objets/*.res
    rm ${root}/codegen/invalid/Objets/*.ass
    rm ${root}/codegen/invalid/Objets/*.res
    echo "done"
    exit 1
else

    nbtests=0
    nbpassed=0

    cd "${root}/codegen/valid/Objets"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac "${f}" && (ima "${file}.ass" > "${file}.res")
        if [ -f "${file}.res" ]; then 
        if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
            echo "--- ${file}: PASSED ---"
            ((nbpassed=nbpassed+1))
        else
            echo "--- ${file}: FAILED ---"
            diff "${file}.expected" "${file}.res"
            exit 1 
        fi
        else
        echo "--- ${file}: KO ---"
        exit 1
        fi
        echo
    done

    cd "${root}/codegen/interactive/Objets/valid"

    echo "### TEST READ Objects : $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac "${f}" && (ima "${file}.ass" > "${file}.res") < ./sourceFiles/${file}.txt
        if [ -f "${file}.res" ]; then 
        if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
            echo "--- ${file}: PASSED ---"
            ((nbpassed=nbpassed+1))
        else
            echo "--- ${file}: FAILED ---"
            diff "${file}.expected" "${file}.res"
            exit 1 
        fi
        else
        echo "--- ${file}: KO ---"
        exit 1
        fi
        echo
    done


    cd "${root}/codegen/invalid/Objets"

    echo "### TEST Invalid : $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac "${f}" && (ima "${file}.ass" > "${file}.res")
        if [ -f "${file}.res" ]; then 
        if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
            echo "--- ${file}: PASSED ---"
            ((nbpassed=nbpassed+1))
        else
            echo "--- ${file}: FAILED ---"
            diff "${file}.expected" "${file}.res"
            exit 1 
        fi
        else
        echo "--- ${file}: KO ---"
        exit 1
        fi
        echo
    done

    echo "### SCORE: ${nbpassed} PASSED / ${nbtests} TESTS ###"
fi
