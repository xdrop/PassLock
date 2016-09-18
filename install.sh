#!/bin/bash
ALIASNAME="plock"
JARNAME="passlock.jar"
BASEDIR=$(dirname "$0")
JARPATH="$BASEDIR/$JARNAME"
DST="/usr/bin/$ALIASNAME"
echo "Symlinking $JARPATH to $DST"
ln -s "$JARPATH" "$DST" 
