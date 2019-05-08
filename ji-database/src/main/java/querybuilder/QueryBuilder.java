package querybuilder;

import database.support.DoubleConsumer;

public abstract class QueryBuilder {

	protected DoubleConsumer consumer;
	
	public QueryBuilder(final DoubleConsumer consumer) {
		this.consumer = consumer;
	}
	
	public abstract DeleteQueryBuilder delete(String table);
	
	public abstract InsertQueryBuilder insert(String table);
	
	public abstract UpdateQueryBuilder update(String table);
	
	public abstract SelectQueryBuilder select(String select);
	
}
