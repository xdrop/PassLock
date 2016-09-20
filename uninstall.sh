#!/bin/sh
echo "Starting uninstall"
rm -rf ~/.passlock/config
read -p "Are you sure you want to remove the datasource? You WILL lose your passwords " -n 1 -r
echo    # move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
   rm -rf ~/.passlock
fi
rm -rf /usr/local/passlock
rm -rf /usr/local/bin/plock
echo "Uninstall complete."
