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

The first step is load a translation file. The file is loaded only once, on first request on key 'module-locale'. Module is string before first dot in translation key or 'messages' as default. See:

Firstly, translator tries load "*module*.*locale-with-country*.properties". If not exists, tries load "*module*.*locale*.properties". If fail, tries "*module*.properties". If still no file founded, tries it again but instead of *module* uses "messages". Otherwise use empty dataset (in this case, the translator returns translation key).

#### Example

Suppose translation key looks like: "first.second.third" and "Locale" is 'xy_XY'. So module name is "first".

<table>
	<tr>
		<th>Looking for file</th>
		<th>Key in file</th>
	</tr>
	<tr>
		<td>first.xy_XY.properties</td>
		<td>second.third</td>
	</tr>
	<tr>
		<td>first.xy.properties</td>
		<td>second.third</td>
	</tr>
	<tr>
		<td>first.properties</td>
		<td>second.third</td>
	</tr>
	<tr>
		<td>messages.xy_XY.properties</td>
		<td>first.second.third</td>
	</tr>
	<tr>
		<td>messages.xy.properties</td>
		<td>first.second.third</td>
	</tr>
	<tr>
		<td>messages.properties</td>
		<td>first.second.third</td>
	</tr>
	<tr>
		<td>*empty dataset*</td>
		<td>*returns first.second.third*</td>
	</tr>
</table>

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