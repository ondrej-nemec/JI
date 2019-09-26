package querybuilder;

import java.sql.SQLException;

public interface InsertQueryBuilder {
	
	InsertQueryBuilder addValue(String columnName, String value);
	
	InsertQueryBuilder addValue(String columnName, boolean value);
	
	InsertQueryBuilder addValue(String columnName, int value);
	
	InsertQueryBuilder addValue(String columnName, double value);
	
	String getSql();
	
	int execute() throws SQLException;

}
