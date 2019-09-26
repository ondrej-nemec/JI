package querybuilder;

import java.sql.SQLException;

public interface DeleteQueryBuilder extends Parameters<DeleteQueryBuilder> {
	
	DeleteQueryBuilder where(String where);
	
	DeleteQueryBuilder andWhere(String where);
	
	DeleteQueryBuilder orWhere(String where);
	
	int execute() throws SQLException;

}
