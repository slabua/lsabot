#! /bin/sed -f
#
# Se la relativa riga dello script e' decommentata,
#+ sostituisce a ciascun . (punto) lo stesso carattere seguito da un new-line,
#+ altrimenti (di default) sostituisce il punto con il new line.
#
# Syntax: lines.sed input-file > output-file

# Se la riga successiva e' commentata, non conserva il punto
#/\./ s//\.\n/g

/\./ s//\n/g
