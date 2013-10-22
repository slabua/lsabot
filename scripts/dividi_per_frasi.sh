#!/bin/bash
#
# Divide in frasi i files dati in input.

echo

docNum=0

echo "Sto analizzando i files..."

echo; echo

for (( a=1; a <= 911; a++ )) #911 nel caso delle frasi AIML di Alice.
do
  docNum=$(($docNum+1));
	cat $1 | head -$docNum | tail -1 > $docNum.frasi 

done

echo

exit 0
