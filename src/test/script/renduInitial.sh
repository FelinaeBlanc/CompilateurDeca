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
    rm ${root}/codegen/valid/renduInitial/*.ass
    rm ${root}/codegen/valid/renduInitial/*.res
    rm ${root}/context/invalid/renduInitial/*.ass
    rm ${root}/context/invalid/renduInitial/*.res
    echo "done"
    exit 1
else

    nbtests=0
    nbpassed=0

    cd "${root}/context/invalid/renduInitial"
    echo "### TEST: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        if decac "${f}" 2> "${file}.res" ; then
        echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.res" > /dev/null ; then
        echo "--- ${file}: PASSED ---"
        ((nbpassed=nbpassed+1))
        else
        echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
        echo "IN \"$(cat ${file}.res)\""
        exit 1
        fi
        echo
    done

    cd "${root}/codegen/valid/renduInitial"
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

    cd "${root}/context/invalid/renduInitial"
    echo "### TEST -r 4: $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        if decac -r 4 "${f}" 2> "${file}.res" ; then
        echo "--- ${file}: KO ---"
        elif grep $(cat "${file}.expected") "${file}.res" > /dev/null ; then
        echo "--- ${file}: PASSED ---"
        ((nbpassed=nbpassed+1))
        else
        echo "--- ${file}: FAILED ---"
            echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
        echo "IN \"$(cat ${file}.res)\""
        exit 1
        fi
        echo
    done

    cd "${root}/codegen/valid/renduInitial"
    echo "### TEST -r 4 : $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac -r 4 "${f}" && (ima "${file}.ass" > "${file}.res")
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
