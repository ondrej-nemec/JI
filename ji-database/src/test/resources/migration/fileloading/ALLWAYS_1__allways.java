package migration.fileloading;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.Join;
import querybuilder.QueryBuilder;

public class ALLWAYS_1__allways implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder
			.createView("first_to_second")
			.select("f.id first_id, s.id second_id, f.name, s.second_name")
			.from("First_table f")
			.join("Second s", Join.FULL_OUTER_JOIN, "f.id = s.first_id")
			.execute();
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		builder.deleteView("first_to-second").execute();
	}

}
