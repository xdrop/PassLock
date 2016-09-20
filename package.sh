#!/bin/sh
mvn clean package
mv -f target/passlock-1.0-SNAPSHOT-jar-with-dependencies.jar bin/passlock.jar
cd bin && cat javastub.sh passlock.jar > plock
chmod +x plock
cd ..
mkdir -p distribution/bin
cp bin/plock distribution/bin/plock
mkdir -p distribution/config
cp config/passlock.config distribution/config/passlock.config
cp install.sh distribution/
zip -r passlock.zip distribution/* 

