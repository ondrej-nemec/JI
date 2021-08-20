package migration.endToEndModule1;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.enums.ColumnType;
import querybuilder.QueryBuilder;

public class V1__AddedFirstTable implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.createTable("First_table_1").addColumn("id", ColumnType.integer()).execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.deleteTable("First_table_1").execute();
	}

}
