# JI project

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Project contains some useful libraries, functions and structures

## Contains

* [Common](ji-common) - Often used logic, useful structures and exceptions.
* [Access control list](ji-acl) - For checking permissions. Not for sign in/out.
* [Translator](ji-translate) - Package supports multi-language applications,
* [Logging](ji-logging) - Implementation Logger interface from Common.
* [Database](ji-database) - Contains: SQL query builder a database migrations.
* [Database tests](ji-testing) - Allow create automatic tests with database.
* [Server-Client communication](ji-communication) - Provide server/client communication (secured or not secured) using java sockets. Implements web server too. 
* [File reading/writing](ji-files) - Factories for Buffered reader/writer and JSON stream

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. Allow you include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:Tag'
```