package querybuilder;

import java.sql.SQLException;

public interface UpdateQueryBuilder extends Parameters<UpdateQueryBuilder> {
	
	UpdateQueryBuilder set(String update);
	
	UpdateQueryBuilder where(String where);
	
	UpdateQueryBuilder andWhere(String where);
	
	UpdateQueryBuilder orWhere(String where);
	
	int execute() throws SQLException;
}
