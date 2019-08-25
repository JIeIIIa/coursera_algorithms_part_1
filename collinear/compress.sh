#!/usr/bin/env bash
FILENAME=collinear

echo "Deleting ../submission/$FILENAME.zip"
rm -f ../submission/$FILENAME.zip

echo "Compressing files"
zip $FILENAME Point.java BruteCollinearPoints.java FastCollinearPoints.java

echo "Moving file"
mv ./$FILENAME.zip ../submission/

echo Ok! `date +%H:%M:%S`
