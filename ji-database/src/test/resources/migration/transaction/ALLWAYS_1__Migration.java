package migration.transaction;

import java.sql.SQLException;

import migration.Migration;
import querybuilder.QueryBuilder;

public class ALLWAYS_1__Migration implements Migration {

	@Override
	public void migrate(QueryBuilder builder) throws SQLException {
		builder.createView("viewName");
	}

	@Override
	public void revert(QueryBuilder builder) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
