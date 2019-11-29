package migration.transaction;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class Working__Migration implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.select("*");
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.select("*");
	}

}
