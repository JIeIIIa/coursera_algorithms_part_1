#!/usr/bin/env bash
FILENAME=8puzzle

echo "Deleting ../submission/$FILENAME.zip"
rm -f ../submission/$FILENAME.zip

echo "Compressing files"
zip $FILENAME Board.java Solver.java

echo "Moving file"
mv ./$FILENAME.zip ../submission/

echo Ok! `date +%H:%M:%S`
