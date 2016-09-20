cat create_release.json | sed "s/tagname/$1/;s/releasename/$2/;s/releasedesc/$3/" > req.json
echo Password: 
read -s password
uploadid=$(curl -i -u xdrop:$password -XPOST "https://api.github.com/repos/xdrop/passlock/releases" -d "@req.json" |  grep upload_url | grep -Eo "[0-9]+")
curl -1 -i -u xdrop:$password -XPOST  \
     -H "Accept: application/vnd.github.manifold-preview" \
     -H "Content-Type: application/zip" \
     --data-binary "@$4" \
     "https://uploads.github.com/repos/xdrop/passlock/releases/$uploadid/assets?name=passlock-$1.zip"
