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
    rm ${root}/codegen/interactive/sansObjets/valid/*.ass
    rm ${root}/codegen/interactive/sansObjets/valid/*.res
    rm ${root}/codegen/interactive/sansObjets/invalid/*.ass
    rm ${root}/codegen/interactive/sansObjets/invalid/*.res
    rm ${root}/codegen/valid/sansObjets/*.ass
    rm ${root}/codegen/valid/sansObjets/*.res
    rm ${root}/codegen/invalid/sansObjets/*.res
    rm ${root}/codegen/invalid/sansObjets/*.ass
    rm ${root}/context/invalid/renduInter/*.res
    echo "done"
    exit 1
else

    nbtests=0
    nbpassed=0



    echo "### TEST -p $(pwd) ----ALL---- ###"
    rm -f *.res *.ass || exit 1
    for d in ${root}/*/valid/*/ ; do
        cd "${d}"
        for f in *.deca ; do
            file="${f%.deca}"
            ((nbtests=nbtests+1))
            decac -p "${f}" > "${file}_inter.dec"
            test_synt "${file}_inter.dec"  | sed 's/\[[0-9]*, [0-9]*\]//g' > "${file}.res"
            test_synt "${f}"  | sed 's/\[[0-9]*, [0-9]*\]//g' > "${file}_synt.expected"
            if [ -f "${file}.res" ]; then 
                if diff -b -q "${file}.res" "${file}_synt.expected" > /dev/null; then
                    echo "--- ${file}: PASSED ---"
                    ((nbpassed=nbpassed+1))

                else
                    echo "--- ${file}: FAILED ---"
                    diff "${file}_synt.expected" "${file}.res"
                fi
            else
                echo "--- ${file}: KO ---"
                exit 1
            fi
        done
    done


    cd "${root}/codegen/valid/sansObjets"
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

    echo "### TEST -r 4: $(pwd) ###"
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

    echo "### TEST -P : $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac -P "${f}" && (ima "${file}.ass" > "${file}.res")
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

    echo "### TEST -P 4: $(pwd) ###"
    decac -P ./*.deca

    cd "${root}/codegen/invalid/sansObjets"

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



    cd "${root}/codegen/interactive/sansObjets/valid"

    echo "### TEST READ : $(pwd) ###"
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

    cd "${root}/codegen/valid/sansObjets"
    echo "### TEST -n : $(pwd) ###"
    rm -f *.res *.ass || exit 1
    for f in *.deca ; do
        file="${f%.deca}"
        ((nbtests=nbtests+1))
        decac -n "${f}" && (ima "${file}.ass" > "${file}.res")
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
    
