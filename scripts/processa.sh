#!/bin/bash
#
# Vengono analizzati i files di testo in $PWD e vengono processati da 
#+ processa.pl
#
# Syntax: processa.sh input-file output-file

echo

echo "Sto analizzando i files..."

echo; echo 

cat $1 | ./preprocess.pl > $2

echo "L'output e' stato rediretto sul file:" $2

echo

exit 0
