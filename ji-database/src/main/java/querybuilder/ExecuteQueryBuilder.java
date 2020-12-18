package querybuilder;

import java.sql.SQLException;

public interface ExecuteQueryBuilder extends Batch {

	void execute() throws SQLException;
	
	String getSql();
	
	@Override
	default String getQuerySql() {
		return getSql();
	}
	
}
