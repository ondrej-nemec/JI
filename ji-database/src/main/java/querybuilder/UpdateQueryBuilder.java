package querybuilder;

import java.sql.SQLException;

public interface UpdateQueryBuilder {
	
	UpdateQueryBuilder set(String update);
	
	UpdateQueryBuilder where(String where);
	
	UpdateQueryBuilder andWhere(String where);
	
	UpdateQueryBuilder orWhere(String where);
	
	UpdateQueryBuilder addParameter(String value);
	
	String getSql();
	
	int execute() throws SQLException;
}
