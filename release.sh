if [ "$#" -le 1 ]; then
  echo "Version missing " >&2
  exit 1
fi
cat src/main/java/me/xdrop/passlock/commands/HelpCommand.java | sed "s/PassLock [0-9][0-9]*.[0-9][0-9]*.[0-9][0-9]*\(-[a-zA-Z]*\)*/PassLock $1/" > tmp && mv tmp src/main/java/me/xdrop/passlock/commands/HelpCommand.java
mvn versions:set -DnewVersion=$1
sh package.sh
mv passlock.zip passlock-$1.zip
git add -u
git commit -m "[release] $1"
git tag $1
git push --tags
sh scripts/github_release.sh $1 $1 $2 "../passlock-$1.zip"
