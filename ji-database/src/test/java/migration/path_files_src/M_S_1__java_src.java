package migration.path_files_src;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class M_S_1__java_src implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.createTable("src_java").execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {}

}
