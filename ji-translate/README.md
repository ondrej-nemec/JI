# JI Translator

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Package supports multi-language applications.

* [Download](#include-in-your-project)
* [Using](#using)
* [How it works](#how-it-works)
	* [Finding translations keys](#finding-a-key)
	* [Using variables](#using-variables)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. Allow you include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-translate:Tag'
```

## Using

Create Translator instance.

```java
Translator translator = PropertiesTranslator.create(
	logger, // implementation of Logger from ji-common
	"path-to-first-properties-file", "second-to-first-properties-file" // from 0 to n
);
```

Translator is created with default java Locale (from `Locale.getDefault()`). Can be changed by:

```java
Locale locale = new Locale("en");
translate.setLocale(locale); // set given locale to this Translator instance
translate.withLocale(locale); // return new Translator instance with given locale
```

Translate message

```java
String key = "";
 Map<String, String> variables = new HashMap<>();
translator.translate(key, variables);
translator.translate(key, variables, locale);
```

## How it works

### Finding a key

Expect key looks like: "first.second.third"

Firstly, translator tries load "*first*.*locale*.properties" file. If not exists, tries load "*first*.properties". If finds one of this files, returns value on "second.third" key.

Secondary, if no value founded, tries load "messages.*locale*.properties" file. If not exists, tries load "messages.properties". If finds one of this files, returns value on "first.second.third" key.

The last step, if everything fail, returns key, that means "first.second.third".

### Using variables

Example of "messages.properties"

```java
my.name=My name is %name% and I am %age% years old
```

```java
Map<String, String> variables = new HashMap<>();
variables.put("name", "John Smith");
translator.translate("my.name", variables); // returns "My name is John Smith and I am %age% years old"

Map<String, String> variables = new HashMap<>();
variables.put("name", "John Smith");
variables.put("age", "42");
translator.translate("my.name", variables); // returns "My name is John Smith and I am 42 years old"
```