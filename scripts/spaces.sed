#! /bin/sed -f
#
# Effettua alcune correzioni nello stream con una frase su ogni linea.
#
# Syntax: spaces.sed input-file > output-file

# delete leading whitespaces
#s/^[ \t]*//
/^[ \t]*/ s///

# delete trailing whitespaces and tab
#s/[ \t]*$//
/[ \t]*$/ s///

# delete all blank lines
/^$/d
/^[\r\n]*$/d

# unquote
#s/^> //
/^> / s///

:a
# remove HTML tags and duplicated spaces
#s/<[^>]*>//g;/</N;//ba
/<[^>]*>/ s///g
/  */ s// /g
/</N
//ba

# delete initial ./!/?
/^[\.\!\?]$/d

# delete initial isolated numbers/letters
/^[0-9A-Za-z][\.]$/ s///
#/^\ [\n\r]$/d
//ba
