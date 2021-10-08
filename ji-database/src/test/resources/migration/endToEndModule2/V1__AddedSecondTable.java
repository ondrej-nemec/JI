package migration.endToEndModule2;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.enums.ColumnType;
import querybuilder.QueryBuilder;

public class V1__AddedSecondTable implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.createTable("Second_table").addColumn("id", ColumnType.integer()).execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.deleteTable("Second_table").execute();
	}

}
