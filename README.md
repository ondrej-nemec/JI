# JI project

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Project contains some useful libraries, functions and structures

## Contain

* [Common](ji-common)
	Mainly useful structures. Will be merged with Utils in the futute.
* [Utils](ji-utils)
	Mainly useful functions. Will be merged with Common in the future.
* [Access control list](ji-acl)
* [Translator](ji-translate)
* [Logging](ji-logging)
* [Database](ji-database)
* [Database tests](ji-testing)
* [Server-Client communication](ji-communication)
* [File reading/writing](ji-files)

## Include in project

JI uses for publication <a href="https://jitpack.io/">JitPack</a> service. Allow you include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:Tag'
```