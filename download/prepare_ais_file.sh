#!/bin/bash

FILENAME=$1

if [ ! -f "${FILENAME}" ]; then
    echo "${FILENAME} does not exists"
    exit 1
fi

DESTINATION_FILENAME="$(basename "$FILENAME" .csv).transformed.csv"

echo "Processing $FILENAME and generating $DESTINATION_FILENAME ..."

## Exclude the first line containing the field names and
## generate UUIDs for each register using uuidgen

grep -v "MMSI,BaseDateTime" $FILENAME | \
 awk -F$',' '("uuidgen" | getline uuid) > 0 {t=$2;gsub("T", " ",t);print t ".000+0000," uuid "," $1 "," $8 "," $3 "," $4} {close("uuidgen")}' > ${DESTINATION_FILENAME}

echo "Done!"
