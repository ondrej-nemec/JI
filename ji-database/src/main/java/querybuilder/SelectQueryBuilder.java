package querybuilder;

import java.util.List;

import database.support.DatabaseRow;

public interface SelectQueryBuilder {

	SelectQueryBuilder from(String table);
	
	SelectQueryBuilder join(String table, Join join, String on);

	SelectQueryBuilder where(String where);
	
	SelectQueryBuilder andWhere(String where);
	
	SelectQueryBuilder orWhere(String where);
	
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
