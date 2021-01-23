package migration.path_files_res;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class M_R_1__java_res implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.createTable("res_java").execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {}

}
