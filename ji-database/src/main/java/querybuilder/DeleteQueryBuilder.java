package querybuilder;

import java.sql.SQLException;

public interface DeleteQueryBuilder {
	
	DeleteQueryBuilder where(String where);
	
	DeleteQueryBuilder andWhere(String where);
	
	DeleteQueryBuilder orWhere(String where);
	
	DeleteQueryBuilder addParameter(String value);
	
	String getSql();
	
	int execute() throws SQLException;

}
