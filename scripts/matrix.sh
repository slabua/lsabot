#!/bin/bash
#
# Esegue la classe OccurrMM su tutti i files delle occorrenze dei termini nei
#+ documenti per generare il corpo della matrice termine-documento.
#
# Syntax: matrix.sh

echo

docNum=0
val=1574

echo "Sto analizzando i files..."

echo; echo

#echo "%%MatrixMarket matrix coordinate real general" > $1
#echo "101509 2484 1332515" >> $1

for file in *.occtab
#for (( i=1; i<=910; i++ )) #2484 oppure 1574 oppure 910
do
  docNum=$(($docNum+1));
  val=$(($val+1));
  java lsabot.ttmatrix.OccurrMM $docNum.occtab termini_tot_FINAL $1 $val
done

echo

exit 0
