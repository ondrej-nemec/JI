# JI - Testing

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

JI Testing library allow you to do tests with database in test environment. Before each test do DB migrations (using <a href="../ji-database">JI Database</a> is nessessary) for up to date structure and inserts data specified by you. After test clears database.

* [Download](#include-in-your-project)
* [Using](#using)
	* [Dataset](#dataset)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. Allow you include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```

And this line to dependencies for using in main code
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-testing:Tag'
// for tests only
testImplementation 'com.github.ondrej-nemec:javainit:ji-testing:Tag'
```

Or this for using in tests only
```gradle
testImplementation 'com.github.ondrej-nemec:javainit:ji-testing:Tag'
```

## Using

The class (or classes) that contains your tests have to extends class `DatabaseTestCase`. You have to implement at least one of constructors:

```java
public MyDbTests(DatabaseConfig config) {
	super(config);
}
```

Or you can use constructor with `Env` class (wrapper for `Map`, see <a href="../ji-common">JI Common</a>). 

```java
public MyDbTests(Env env) {
	super(env);
}
```

In Env Properties or configuration file are required this keys (refer to DatabaseConfig):

```
db.type
db.pathOrUrl
db.externalServer
db.schema
db.login
db.password
# one or more folders separated by comma (,)
db.pathToMigrations
db.timezone
db.poolSize
```

**NOTE:** Remember, `DatabaseTestCase` uses `@Before` and `@After` method annotation for keeps database consistent. If you wish use this annotations **always** call parent:

```java
// Before
@Before
public void myBefore() throws SQLException {
	super.before();
	// TODO my own code
}

// After
@After
public void myAfter() throws SQLException {
	// TODO my own code
	super.after();
}
```

### Dataset

Your test have to implements `getDataSet` method, too. There is example how could this method looks like:

```java
@Override
protected List<Table> getDataSet() {
	return Arrays.asList(
		// insert into table 'users' this rows
		new Table("users", Arrays.asList(
			// insert into table 'users' into column 'name' value "User #1"
			new Row()
				.addColumn("name", "User #1")
				.addColumn("age", 21),
			new Row()
				.addColumn("name", "User #2")
				.addColumn("age", 22)
		)),
		new Table("cars", Arrays.asList(
			new Row()
				.addColumn("type", "personal")
				.addColumn("brand", "*****")
		))
	);
}
```