package migration.doMigrations;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class V1__first implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.select("a");
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.select("b");
	}

}
