#!/bin/bash
#
# Copia in files numerati i documenti con estensione ".txt".

echo

docNum=0

echo "Sto analizzando i files..."

echo; echo

for file in *.txt
do
  docNum=$(($docNum+1));
	cat $file > $docNum.num

done

echo

exit 0
