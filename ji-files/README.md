# JI Files

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

JI Files contains functionality for reading and writing not structures files and JSON. For not structured files offer factories for Reader/Writer and some most often required functions.

* [Download](#include-in-your-project)
* [Not structured files](#not-structured-files)
	* [Binary files](#binary-files)
	* [Plain text files](#plain-text-files)
	* [Common read and write functions](#common-read-and-write-functions)
* [JSON](#json)
	* [JSON Stream](#json-stream)
	* [Objects and JSON](#objects-and-json)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. It allows you to include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-files:Tag'
```

## Not structured files

There are classes that create required writer/reader from stream or file, accept `ThrowingConsumer` given by you and close resources.

### Binary files

`DataOutputStream` is create from `OutputStream`

```
Binary.get().write((dataOutputStream)->{
	// your logic
}, outputStream);
```

`DataInputStream` can be created from file path (in String) or from `InputStream`.

```
Binary.get().write((dataOutpuStream)->{
	// your logic
}, "filename");

Binary.get().read((dataInputStream)->{
	// your logic
}, inputStream);
```

### Plain text files

For reading there is support for creating `BufferedReader` from:
* file path in String
* file path in String and charset (in String)
* `File`
* `File` and charset
* `InputStream`
* `InputStream` and charset
* `URL`
* `URL` and charset

For example for path and charset:

```
Text.get().read((bufferedReader)->{
	// your logic
}, "/some-file/path", "utf-8");
```

For writing can be created `BufferedWritter` from:

* file path in String and boolean parameter "append to end of file"
* file path in String, charset and boolean parameter "append to end of file"
* `OutputStream`
* `OutputStream` and charset

Example shows writing to path with charset. Content rewrite file.

```
Text.get().write((bufferedWriter)->{
	// your logic
}, "/some-file/path", "utf-8", false);
```

### Common read and write functions

For simple reading/writing JI Files contains `ReadText` and `WriteText` classes.

#### ReadText

With this class you can process file content per line. Each line is readed, then is applied you Consumer and line is discarted (from memory not from file):

```
ReadText.get().perLine(bufferedReader, (actualLine)->{
	// your logic
});
```

Loads whole file content to one String:

```
String content = ReadText.get().asString(buffererReader);
```

Loads file to list - one list item one row:

```
List<String> content = ReadText.get().asList(bufferedReader);
```

You can load file content to 'List in List' - some primitive table. First list means rows, second means columns in that row:

```
List<List<String>> content = ReadText.get().asTable(bufferedReader, ","); // each line will be splited by comma
```

Writing is opposite of reading: write string, list of string (new line is added automatically) and 'list in list':

```
WriteText.get().write(bufferedWriter, "some text");
WriteText.get().write(bufferedWriter, list);
WriteText.get().write(bufferedWriter, listInList, ","); // write simply csv :-)
```

## JSON

### JSON stream

#### InputJsonStream

For reading data from JSON you need `InputProvider`. You can you prepared `InputStringProvider` or implement your own.

```
InputProvider provider = new InputStringProvider(json);
InputJsonStream stream = new InputJsonStream(provider);
```

Now just call `next` function. This function returns `Event` object.

This object always contains `EventType` (Object Start, Object End, Object Item, List Start, List End, List Item, Document Start and Document End). If you wish read whole JSON, just call `next` in while cycle until Event Type is Document End.

If is Stream actually iterate over object, you can read name of key/name.

If Event is Object Item or List Item, the Event contains a Value. The Value contains real value and enum of type (boolean, integer, double, string). 

#### OutputJsonStream

With `OutputJsonStream` you can write JSON. Again you need `OutputStringProvider`. Prepared are `OutputStringProvider` and `OutputWriterProvider`.

```
OutputStringProvider provider = new OutputStringProvider();
OutputJsonStream stream = new OutputJsonStream(provider);
```

Stream is ready for writing events.
For Example:

```
stream.startDocument();
stream.writeObjectValue("some-int", 12);

stream.writeListStart("list.name");
stream.writeListValue("first-value");
stream.writeListValue(false);
stream.writeListValue(null);
stream.writeListEnd();

stream.writeObjectStart("object-name");
stream.writeObjectValue("inside-object", "value");
stream.writeObjectEnd();

stream.endDocument();
```

### Objects and JSON

Using Streams for reading/writing JSON can be useful but there is easiest way to do it. JI Files allow you parse Object to JSON and JSON to Object. There are rules:

* `java.utils.Map` and `MapDictionary` are JSON Oject
* `java.utils.List` and `ListDictionary` are JSON List
* primitives (excepts byte), object for primitives (Integer, Double, Boolean, ...) and Strings stay as they are
* for writing only:
	* object that implements `Jsonable` are saved with `Jsonable::toJson`
	* for others object the java reflection is used
	
#### Reflection for writing

By default, all class parameters are saved with their names. If you wish exclude some parameter, just annotate it with `JsonIgnored`. If you wish use another name, annotate it with `JsonParameter`.

#### Reading

```
JsonReader reader = new JsonReader();
```

Reading full JSON file. Returns `Map<String, Object>`

```
reader.readJson(String json);
reader.readJson(InputJsonStream stream); // if you need another that StringProvider
```

Reading something what is JSON but not sure if main structure is object. Returns Object.

```
reader.read(String json);
reader.read(InputJonsStream stream);
```

#### Writing

```
JsonWritter writter = new JsonWritter();
```

Creating JDON from Object and returning to you:

```
String json = writter.write(Object toJson);
```

Writing JSON to given `OutputJsonStream`:

```
writter.write(OutputJsonStream stream, Object toJson);
writter.writeJson(OutputJsonStream stream, Map<String, Object> toJson);
```