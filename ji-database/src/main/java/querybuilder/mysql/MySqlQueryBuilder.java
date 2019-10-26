package querybuilder.mysql;

import java.sql.Connection;

import querybuilder.DeleteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlQueryBuilder extends QueryBuilder {

	public MySqlQueryBuilder(Connection connection) {
		super(connection);
	}

	@Override
	public DeleteQueryBuilder delete(String table) {
		return new MySqlDeleteBuilder(connection, table);
	}

	@Override
	public InsertQueryBuilder insert(String table) {
		return new MySqlInsertBuilder(connection, table);
	}

	@Override
	public UpdateQueryBuilder update(String table) {
		return new MySqlUpdateBuilder(connection, table);
	}

	@Override
	public SelectQueryBuilder select(String select) {
		return new MySqlSelectBuilder(connection, select);
	}

}
