package querybuilder;

import java.sql.SQLException;

public interface CreateViewQueryBuilder extends Parameters<CreateViewQueryBuilder> {
	
	CreateViewQueryBuilder select(String... params);

	CreateViewQueryBuilder from(String table);
	
	CreateViewQueryBuilder join(String table, Join join, String on);

	CreateViewQueryBuilder where(String where);
	
	CreateViewQueryBuilder andWhere(String where);
	
	CreateViewQueryBuilder orWhere(String where);
	
	CreateViewQueryBuilder orderBy(String orderBy);
	
	CreateViewQueryBuilder groupBy(String groupBy);
	
	CreateViewQueryBuilder having(String having);
	
	CreateViewQueryBuilder limit(int limit);
	
	CreateViewQueryBuilder offset(int offset);
	
	void execute() throws SQLException;
	
	String getSql();
	
}
