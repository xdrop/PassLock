# passlock
### Simple command line password manager (with style!)
[![Build Status](https://travis-ci.org/xdrop/PassLock.svg?branch=master)](https://travis-ci.org/xdrop/passlock)
[![GitHub release](https://img.shields.io/github/release/xdrop/passlock.svg?maxAge=3600)](https://github.com/xdrop/PassLock/releases)

[![Screen capture](http://xdrop.me/img/passlock-terminal.png)](https://github.com/xdrop/passlock)

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
unzip passlock-<version>.zip -d passlock
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

