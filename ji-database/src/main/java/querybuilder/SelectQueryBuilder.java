package querybuilder;

import querybuilder.join.Join;

public interface SelectQueryBuilder {

	SelectQueryBuilder select(String select);

	SelectQueryBuilder from(String table);
	
	SelectQueryBuilder join(String table, Join join, String on);

	SelectQueryBuilder where(String where);
	
	SelectQueryBuilder and(String where);
	
	SelectQueryBuilder orderBy(String orderBy);
	
	SelectQueryBuilder groupBy(String groupBy);
	
	SelectQueryBuilder having(String having);
	
	SelectQueryBuilder limit(int limit);
	
	SelectQueryBuilder offset(int offset);
	
	SelectQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	//TODO returns
	
}
