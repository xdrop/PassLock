#!/bin/sh
mvn clean package
mv -f target/passlock-1.0-SNAPSHOT-jar-with-dependencies.jar bin/passlock.jar
cd bin && cat javastub.sh passlock.jar > plock
cd ..
mkdir -p distribution/bin
cp plock distribution/bin/plock
mkdir -p distribution/config
mv config/passlock.config distribution/config/passlock.config
zip -r passlock.zip distribution/* 
