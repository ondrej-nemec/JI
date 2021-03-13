# JI - Database

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

JI Database package allow you establish connection to database, use connection pool, easy transactions. Next, provide query builder (**not ORM**) for MySQL, SQL Server, PostgreSql and Derby database.

* [Download](#include-in-your-project)
* [Usage](#usage)
	* [Get connection](#get-connection)
	* [Query Builder](#query-builder)
	* [Migrations](#migrations)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. Allow you include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-database:Tag'
```

## Usage

### Get connection

Firstly, create instance of `Database` class using `DatabaseConfig`:

```java
DatabaseConfig config = new DatabaseConfig(
	"database-type", // "mysql" or "derby" or "postgresql" or "sqlserver"
	"//localhost", // URL to server
	true, // if server is not manage by this library
	"db-name", // name of your database. This database does not have to exists
	"username", // username
	"", // password
	Arrays.asList(), // list of migration folders. See bellow
	 "Europe/London", // timezone 
	 50 // connection pool size
);
Database database = new Database(config, logger); // logger is instance of Logger interface from ji-common
```

**NOTE 1:** Managing DB server from JI Database is not full supported yet. So, as the third parameter of `DatabaseConfig` please use allwais `true`.

**NOTE 2:** You still need add correct jdbc library to your project.

**NOTE 3:** If you wish use Windows Authentication, let username and password empty. And at the end of server URL add `;integratedSecurity=true`. You will probably need add some library to you java folder. 

`Database` instance allow you create database (in not exists) and/or do all migrations (see (#migrations)).

```java
// only create DB
database.createDbIfNotExists();
// only migrate
database.migrate();
// create DB and migrate
boolean success = database.createDbAndMigrate();
```

You can do queries to database with two ways: with `Connection` (java class) or with `QueryBuilder` (see #query-builder).

```java
// with connection
database.applyQuery((connection)->{
	// do what you wish
	return null; // return what you wish :-)
});
database.applyBuilder((builder)->{
	// do what you wish
	return null; // return what you wish :-)
});
```

**Note:** Everything what you do inside this two methods is automatically transaction. If any Exception occurs, is catched, transaction is rollbacked and the exception is thrown again.

### Query Builder

`QueryBuilder` class creates SQL query by given configuration. As a bonus allow use named parameters instead of using "?". A instance of this class you get in database (method `applyBuilder`).

#### Using 'where'

Some Query Builders (especially Select Query Builder) have `where`, `andWhere` and `orWhere` methods. How it works? `where` must be first and maximal one, others are up to you. Everything inside argument (for all three) is automatically in brackets:

* `where("id = 4")` will be `WHERE (id = 4)`
* `where("id = 5").andWhere("id < 20")` will be `WHERE (id = 5) AND (id < 20)`
* `where("id = 4").orWhere("id = 5")` will be `WHERE (id = 4) OR (id = 5)`
* `where("id = 4 or id = 5").andWhere("age > 18")` will be `WHERE (id = 4 or id = 5) AND (age > 18)`

#### Parameters

With Query Builder you can use parameters in SQL. This parameters replace some key in SQL with given value. **This value is escaped.** If value is instance of `Iterable`, Query Builder escape each value separately and connects them to one string separated by comma (useful f.e. for `in` clause, see examples).

#### Examples

Simple query:

```java
// returns List<DatabaseRow>
database.applyBuilder((builder)->{
	return builder.select("*").from("users").fetchAll();
});
```

Query with parameters:

```java
// returns List<Integer>
database.applyBuilder((builder)->{
	return builder.select("id").from("users)
		.where("id > :id OR id < :id")
		.addParameter(":id", id)
		.fetchAll((databaseRow)->{
			return databaseRow.getInteger("id");
		});
});
```

Query with Iterable parameter:

```java
database.applyBuilder((builder)->{
	return builder.select(*).from("users)
		.where("id in (:user_ids)")
		.addParamter(":user_ids", Arrays.asList(1,2,3,4))
		.fetchAll();
});
```

### Known issues

* Derby - alter table
* SQL Server - alter table - The operation 'ALTER TABLE DROP INDEX' is supported only with memory optimized tables

### Migrations

Database migrations or simply migrations are tool allowing you keep database in same state on production and on develop with your colleagues. In migrations folders (in `DatabaseConfig`) are scripts for updating current database. Every structure change have to by in this scripts so after migrating you have your database up to date to current code. After migrating each script this tool make note to special table and not migrate this script again.

There is name convention for migration files: *id*\_\_*description*. *id* must be unique for folder, cannot contains '\_\_'. Suggested *id* format is: *yyyy_mm_dd_i* where *i* is index of migration that day.

JI Database support two types of database:

* SQL - simple SQL script
* Java - have to implements `Migration` interface

#### Always migrations

Special migrations where id contains *ALWAYS* are "Always migrations". This migrations are migrated after others migrations **everytime** and tool make no note. This is very helpful in case you use db views: You create view with standart migration like "create view ... as select 'a'" and create Always migration with altering this view. Now you have this view in git as a change. Without this you had have to create new migration for every single view change.

