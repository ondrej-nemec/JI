package querybuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import database.support.DatabaseRow;

public interface SelectQueryBuilder extends Parameters<SelectQueryBuilder> {

	SelectQueryBuilder from(String table);
	
	SelectQueryBuilder join(String table, Join join, String on);

	SelectQueryBuilder where(String where);
	
	SelectQueryBuilder andWhere(String where);
	
	SelectQueryBuilder orWhere(String where);
	
	SelectQueryBuilder orderBy(String orderBy);
	
	SelectQueryBuilder groupBy(String groupBy);
	
	SelectQueryBuilder having(String having);
	
	SelectQueryBuilder limit(int limit, int offset);
	
	String fetchSingle() throws SQLException;
	
	DatabaseRow fetchRow() throws SQLException;
	
	List<DatabaseRow> fetchAll() throws SQLException;
	
	List<String> fetchAll(Function<DatabaseRow, String> function) throws SQLException;

}
