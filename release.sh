mvn versions:set -DnewVersion=$1
sh package.sh
mv passlock.zip passlock-$1.zip
