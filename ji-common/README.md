# JI - Common

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Often used logic, useful structures and exceptions.

* [Download](#include-in-your-project)
* [Contains](#contains)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. It allows you to include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-common:Tag'
```

## Contains

* Exceptions
	* LogicException
	* NotImplementedYet
* Structures
	* Dictionary
	* ThrowingConsumer
	* ThrowingBiConsumer
	* ThrowingFunction
	* ThrowingBiFunction
	* ThrowingSupplier
	* Tuple2
	* Tuple3
	* UniqueMap
	* Logger - interface used in all libraries of this project
* Functions
	* Console
	* DateTime
	* Implode - from Iterable or array make string with given glue. Like `implode` function from PHP 
	* Dictionary
	* ListDictionary
	* MapDictionary
	* OperationSystem
	* Env
	* Terminal
	* Slugify
	* FileExtension - can extract extension from file name
	* FilesList - get list of file names from given folder. Folder can be in file system or in jar.
	* InputStreamLoader - create input stream from given path. Firstly try load from resources, if not file found, tries file system
	* PropertiesLoader - load properties from file using `InputStreamLoader`