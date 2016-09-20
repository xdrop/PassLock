#!/bin/sh
mvn clean package
mv -f target/*-jar-with-dependencies.jar bin/passlock.jar
cd bin && cat javastub.sh passlock.jar > plock
chmod +x plock
cd ..
mkdir -p distribution/bin
cp bin/plock distribution/bin/plock
mkdir -p distribution/config
cp config/passlock.config distribution/config/passlock.config
cp install.sh distribution/
cp uninstall.sh distribution/
cp usage distribution/
rm -f passlock.zip 2>/dev/null

cd distribution; zip -r ../passlock.zip * 

