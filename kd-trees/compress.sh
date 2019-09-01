#!/usr/bin/env bash
FILENAME=kd-trees

echo "Deleting ../submission/$FILENAME.zip"
rm -f ../submission/$FILENAME.zip

echo "Compressing files"
zip $FILENAME PointSET.java KdTree.java

echo "Moving file"
mv ./$FILENAME.zip ../submission/

echo Ok! `date +%H:%M:%S`
