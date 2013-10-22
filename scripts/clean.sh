#!/bin/bash
#
# Esegue i passaggi di pulitura dei files con estensione ".frasi".
# Tali files contengono i documenti (frasi o files) da processare.
#
# Syntax: clean.sh

echo

echo "Sto analizzando i files..."

echo; echo

for file in *.frasi
do
  processa.sh $file $file.pre

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  echo "Convertito il file \"$file\"".
done

rm -f *.frasi

echo

for file in *.pre
do
  run.sh $file $file.clean

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  echo "Convertito il file \"$file\"".
done

rm -f *.pre

echo

for file in *.clean
do
  occorrenze.sh $file $file.occ

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  echo "Convertito il file \"$file\"".
done

rm -f *.clean

echo

for file in *.occ
do
  format.sh $file > $file.occtab

  # Commentare la riga successiva per non visualizzare i nomi
  #+ dei files durante la conversione
  echo "Convertito il file \"$file\"".
done

rm -f *.occ

for ((i=1; i<=1574; i++)) #1574 oppure 910
do
  mv $i.frasi.pre.clean.occ.occtab $i.occtab
done

echo

exit 0
