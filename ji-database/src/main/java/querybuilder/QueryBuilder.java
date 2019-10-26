package querybuilder;

import java.sql.Connection;

public abstract class QueryBuilder {

	protected Connection connection;
	
	public QueryBuilder(final Connection connection) {
		this.connection = connection;
	}
	
	public abstract DeleteQueryBuilder delete(String table);
	
	public abstract InsertQueryBuilder insert(String table);
	
	public abstract UpdateQueryBuilder update(String table);
	
	public abstract SelectQueryBuilder select(String select);
	
}
