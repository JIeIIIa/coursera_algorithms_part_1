#!/usr/bin/env bash
FILENAME=percolation

echo "Deleting ../submission/$FILENAME.zip"
rm -f ../submission/$FILENAME.zip

echo "Compressing files"
zip $FILENAME Deque.java RandomizedQueue.java Permutation.java

echo "Moving file"
mv ./$FILENAME.zip ../submission/

echo Ok! `date +%H:%M:%S`
