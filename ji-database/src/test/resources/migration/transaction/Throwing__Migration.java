package migration.transaction;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class Throwing__Migration implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		throw new SQLException();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		throw new SQLException();
	}

}
