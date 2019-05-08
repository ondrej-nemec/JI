package querybuilder;

import java.util.List;

import database.support.DatabaseRow;
import querybuilder.join.Join;

public interface SelectQueryBuilder {

	SelectQueryBuilder from(String table);
	
	SelectQueryBuilder join(String table, Join join, String on);

	SelectQueryBuilder where(String where);
	
	SelectQueryBuilder and(String where);
	
	SelectQueryBuilder or(String where);
	
	SelectQueryBuilder orderBy(String orderBy);
	
	SelectQueryBuilder groupBy(String groupBy);
	
	SelectQueryBuilder having(String having);
	
	SelectQueryBuilder limit(int limit);
	
	SelectQueryBuilder offset(int offset);
	
	SelectQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	String fetchSingle();
	
	DatabaseRow fetchRow();
	
	List<DatabaseRow> fetchAll();
	
}
