#!/bin/bash

IN="$1"
if [ -z "$1" ]; then
	IN=merged.xml
fi
echo "XML: $IN" 1>&2;

OUTDIR="$2"
if [ -z "$2" ]; then
	OUTDIR=.
fi
echo "DIR: $OUTDIR" 1>&2;

java -ea -jar ewn-grind.jar "${IN}" "${OUTDIR}"
