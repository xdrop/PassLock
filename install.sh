#!/bin/bash
SOURCE=bin/plock
TARGET="/usr/bin/plock"
echo "Symlinking $SOURCE to $TARGET"
ln -s "$SOURCE" $TARGET" 
