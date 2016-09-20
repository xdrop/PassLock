#!/bin/bash
SOURCE=bin/plock
TARGET="/usr/local/bin/plock"
echo "Installing PassLock..."
echo "Symlinking to $TARGET..."
mkdir /usr/local/passlock
cp -r . /usr/local/passlock
chmod +x /usr/local/passlock/bin/plock
ln -s "../passlock/bin/plock" "$TARGET"
mkdir ~/.passlock
cp config/passlock.config ~/.passlock/config 
echo "Install succesful.
