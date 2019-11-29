package migration;

import java.sql.SQLException;

import querybuilder.QueryBuilder;

public interface Migration {
	
	void migrate(QueryBuilder builder) throws SQLException;
	
	void revert(QueryBuilder builder) throws SQLException;

}
