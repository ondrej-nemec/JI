package database.mysql;

import database.support.DoubleConsumer;
import querybuilder.DeleteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlQueryBuilder extends QueryBuilder {

	public MySqlQueryBuilder(DoubleConsumer consumer) {
		super(consumer);
	}

	@Override
	public DeleteQueryBuilder delete(String table) {
		return new MySqlDeleteBuilder(consumer, table);
	}

	@Override
	public InsertQueryBuilder insert(String table) {
		return new MySqlInsertBuilder(consumer, table);
	}

	@Override
	public UpdateQueryBuilder update(String table) {
		return new MySqlUpdateBuilder(consumer, table);
	}

	@Override
	public SelectQueryBuilder select(String select) {
		return new MySqlSelectBuilder(consumer, select);
	}

}
