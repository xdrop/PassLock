# passlock
### Simple command line password manager (with style!)
[![Build Status](https://travis-ci.org/xdrop/PassLock.svg?branch=master)](https://travis-ci.org/xdrop/passlock)
[![GitHub release](https://img.shields.io/github/release/xdrop/passlock.svg)](https://github.com/xdrop/PassLock/releases)
[![Dependencies](https://img.shields.io/librariesio/github/xdrop/passlock.svg?maxAge=7200)](https://github.com/xdrop/passlock)
[![Awesomeness](https://img.shields.io/badge/awesomeness-maximum-brightgreen.svg?maxAge=1233333)](https://github.com/xdrop/passlock)

[![Screen capture](http://xdrop.me/img/passlock-cmd.png)](http://xdrop.me/img/passlock-cmd.png)

## Features

- [x] Search for a password `g`,`get`
- [x] Add a new password `a`,`add`
- [x] Update a password `u`, `update`
- [x] Delete a password `rm`, `d`
- [x] Copy a password `cp`, `copy`
- [x] Rename a password `mv`, `r`
- [x] List passwords `ls`
- [x] Fuzzy search for a password quickly

## Installation
```sh
unzip passlock-<version>.zip 
cd passlock
sh install.sh
plock --help
```

## Uninstall
```sh
sh /usr/local/passlock/uninstall.sh
```


## Usage

#### Add a password
```
plock a www.websitepass.com
```

#### Get a password
```
plock g mysite
```

#### Delete a password
```
plock rm www.websitepass.com
```

#### Update a password
```
plock u www.websitepass.com
```

#### List passwords
```
plock ls
```

#### Copy a password
```
plock cp sourcepass targetpass
```

#### Reset datasource
```
plock reset
```

## Live
[![Screen capture](http://xdrop.me/img/passlock.gif)](https://github.com/xdrop/passlock)

