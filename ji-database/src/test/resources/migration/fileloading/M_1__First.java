package migration.fileloading;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.QueryBuilder;

public class M_1__First implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder
			.createTable("First_table")
			.addColumn("id", ColumnType.integer(), ColumnSetting.AUTO_INCREMENT, ColumnSetting.PRIMARY_KEY)
			.addColumn("name", ColumnType.string(40), ColumnSetting.NULL)
			.execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.deleteTable("First_table").execute();
	}

}
