#!/bin/bash
#
# Divide i files in frasi e ne genera le relative occorrenze dei termini.

echo

docNum=0

echo "Sto analizzando i files..."

echo; echo

for (( a=1; a <= 2484; a++ )) #2894 sono tutti i documenti, anche frasi AIML.
do
  docNum=$(($docNum+1));
	cat $1 | head -$docNum | tail -1 > $docNum.txt 

done

for file in *.txt
do
  occorrenze.sh $file $file.occ

  # echo $docNum;

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  # echo "Convertito il file \"$file\"".
done

#rm -f *.txt

for file in *.occ
do
  format.sh $file > $file.occtab

  # echo $docNum;

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  # echo "Convertito il file \"$file\"".
done

rm -f *.occ

echo

exit 0
