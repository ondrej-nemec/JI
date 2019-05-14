package querybuilder;

import java.sql.SQLException;

public interface InsertQueryBuilder {
	
	InsertQueryBuilder addValue(String columnName, String value);
	
	String getSql();
	
	int execute() throws SQLException;

}
