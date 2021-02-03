package querybuilder;

import java.sql.Connection;

import common.Implode;

public abstract class QueryBuilder {

	protected Connection connection;
	
	public QueryBuilder(final Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public BatchBuilder batch() {
		return new BatchBuilder(connection);
	}
	
	/***********************/
	
	public abstract Functions getSqlFunctions();
	
	public abstract DeleteQueryBuilder delete(String table);
	
	public abstract InsertQueryBuilder insert(String table);
	
	public abstract UpdateQueryBuilder update(String table);
	
	public abstract SelectQueryBuilder select(String select);
	
	protected abstract SelectQueryBuilder query(String query);
		
	/**
	 * A ∪ B   Set union: Combine two sets into one
	 * @param union
	 * @return
	 */
	public SelectQueryBuilder union(SelectQueryBuilder ...union) {
		return query(Implode.implode((sql)->sql.createSql(), " UNION ", union));
	}
	
	/**
	 * A ∩ B   Set intersection: The members that A and B have in common
	 * @param select
	 * @return
	 */
	public SelectQueryBuilder intersect(SelectQueryBuilder ...intersect) {
		return query(Implode.implode((sql)->sql.createSql(), " INTERSECT ", intersect));
	}
	
	/**
	 * A − B   Set difference: The members of A that are not in B
	 * @param select
	 * @return
	 */
	public SelectQueryBuilder except(SelectQueryBuilder ...except) {
		return query(Implode.implode((sql)->sql.createSql(), " EXCEPT ", except));
	}
	
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
