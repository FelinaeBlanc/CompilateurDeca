#! /bin/sh

# Auteur : gl07
# Version initiale : 21/04/2023

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

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


if test_context src/test/deca/context/invalid/provided/affect-incompatible.deca 2>&1 | \
    grep -q -e 'affect-incompatible.deca:15:'
then
    echo "Echec attendu pour test_context"
else
    echo "Succes inattendu de test_context"
    exit 1
fi

if test_context src/test/deca/context/valid/provided/hello-world.deca 2>&1 | \
    grep -q -e 'hello-world.deca:[0-9]'
then
    echo "Echec inattendu pour test_context"
    exit 1
else
    echo "Succes attendu de test_context"
fi

nbtests=0
nbpassed=0

cd "${root}/context/invalid/renduInter"
echo "### TEST: $(pwd) ###"
rm -f *.res *.ass || exit 1
for f in *.deca ; do
    file="${f%.deca}"
    ((nbtests=nbtests+1))
    if decac -v "${f}" 2> "${file}.res" ; then
	echo "--- ${file}: KO ---"
    elif grep $(cat "${file}.expected" | cut -d " " -f1 ) "${file}.res" > /dev/null ; then
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