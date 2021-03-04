# JI - Common

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Mainly useful structures. Will be merged with Utils in the futute.

* [Download](#include-in-your-project)
* [Contains](#contains)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. Allow you include this project by using Gradle or Maven.

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
	* ThrowingConsumer
	* ThrowingBiConsumer
	* ThrowingFunction
	* ThrowingSupplier
	* Tuple2
	* Tuple3
	* UniqueMap
	* Logger
* Functions
	* Console
	* DateTime
	* FileExtension
	* Implode
	* MapInit
	* OperationSystem