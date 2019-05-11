package querybuilder;

import java.sql.SQLException;

public interface InsertQueryBuilder {
	
	InsertQueryBuilder addColumns(String... columns);
	
	InsertQueryBuilder values(String... values);
	
	InsertQueryBuilder addParameter(String value);
	
	String getSql();
	
	int execute() throws SQLException;

}
