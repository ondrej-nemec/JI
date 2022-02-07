# JI taskkist

## Known issues

* Database: SQL Server => The operation 'ALTER TABLE DROP INDEX' is supported only with memory optimized tables
* Database: Derb => Alter table create/delete foreign key not working
* File upload requires at least one another input except file - bug in sending
* JSON write object: sometimes infinite loop happends - with some nativ objects

## FIX and Bugs

* Files
    * JSON: escape `map.put("subText2", "\\\"]'); alert('Successfull XSS'); // ");`
    * JSON: check \n character
* Communication:
	* `Content length not match expecation. Expected: 610, actual: 0` - with file upload

## TODO

* Common
	* Env: get part: return Env with access to only part of parent - keys with prefix
	* Substitution for DateTime?
* More unit tests
* Check TODOs - remove or add here
* Database:
    * Known issue: SQL Server: The operation 'ALTER TABLE DROP INDEX' is supported only with memory optimized tables
    * Known issue: Derby: Alter table create/delete foreign key not working
    * FK on* action cascade,delete - not supported in derby, sql server
* Communication:
    * Try trusted certificates (improve doc)
    * Try higher version of TLS (Server class)
    * Try load file and maybe add content type as extension to name for higher security
* Files
    * sometimes infinite loop happends - with some nativ objects

## Improvement

* Full comtability with java 9 - 16
* Common:
     * Mapper – create instance with parametrized constructor
	* Env: load configuration from another files like xml, too.
* Database
	* Migrations: revert - remove or add to documentation
	* QueryBuilder: date and time data time
	* QueryBuilder: add multicolumn primary key, then use in migrations
	* QueryBuilder: create view from MultiSelect
	* DB: start/stop database server if is not external (maybe: https://stackoverflow.com/questions/9075098/start-windows-service-from-java/9075237#9075237)
	* More db
	* Profiler: add execution time
* Communication
    * HTTP v2
    * corrent parsing map in list `?list[][name]=smith.john&list[][name]=doe.jane&list[][name]=my.name`
* Translator
	* Load translation from other file types like xml

## Proposal

* Common:
    * FilesList: jar/bin - modification time
    * Make Interval structure https://stackoverflow.com/questions/13742367/java-suitable-data-structure-for-search-interval/13742836#13742836
    * DateTime: add as feature to dictionary value (toString())
    * Improve and add: OperationSystem, StackTrace, Terminal
    * DictionaryValue: support for serialize (to JSON too), collection/set
* Communication:
    * RestApiClient - own implementation, including websockets
    * SSL: some cache for loading *Store
    * RestApiServer: Parse some request types like xml, html and JS
* Database:
   * Materialization
   * QueryBuilder - high validation - like MultipleSelectBuilder::orderBy
* BIG rewrite
   * separate to more libs of one project (with TOTI?)
   * split Files (plain, json), Database (core, query builder, migrations, testing), Common (dictionary,...)