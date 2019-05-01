package querybuilder;

public interface DeleteQueryBuilder {

	DeleteQueryBuilder delete(String table);
	
	DeleteQueryBuilder where(String where);
	
	DeleteQueryBuilder andWhere(String where);
	
	DeleteQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	void execute();

}
