package querybuilder;

import java.sql.Connection;

public abstract class QueryBuilder {

	protected Connection connection;
	
	public QueryBuilder(final Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	/***********************/
	
	public abstract DeleteQueryBuilder delete(String table);
	
	public abstract InsertQueryBuilder insert(String table);
	
	public abstract UpdateQueryBuilder update(String table);
	
	public abstract SelectQueryBuilder select(String select);
	
	/***********************/
	
	public abstract ExecuteQueryBuilder deleteTable(String table);
	
	public abstract CreateTableQueryBuilder createTable(String name);
	
	public abstract AlterTableQueryBuilder alterTable(String name);
	
	/***********************/
	
	public abstract ExecuteQueryBuilder deleteView(String table);
	
	public abstract CreateViewQueryBuilder createView(String name);
	
	public abstract CreateViewQueryBuilder alterView(String name);
	
	/***********************/
	
	public abstract ExecuteQueryBuilder createIndex(String name, String table, String... colums);
	
	public abstract ExecuteQueryBuilder deleteIndex(String name, String table);

}
