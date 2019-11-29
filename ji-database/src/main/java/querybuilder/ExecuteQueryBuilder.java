package querybuilder;

import java.sql.SQLException;

public interface ExecuteQueryBuilder {

	void execute() throws SQLException;
	
	String getSql();
	
}
