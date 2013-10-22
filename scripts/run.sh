#! /bin/bash
#
# Mantenere i files run.sed, lines.sed, spaces.sed nella stessa directory
#
# Syntax: run.sh input-file output-file

./lines.sed $1 | ./spaces.sed > $2
