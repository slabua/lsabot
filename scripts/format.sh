#! /bin/sed -f
#
# Elimina gli spazi ripetuti iniziali e trasforma lo spazio
#+ centrale in carattere di tabulazione.
#
# Syntax: format.sh input-file > output-file

/^\ */ s///g
/\ / s//\t/g
