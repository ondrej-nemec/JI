package querybuilder;

public interface DeleteQueryBuilder {
	
	DeleteQueryBuilder where(String where);
	
	DeleteQueryBuilder andWhere(String where);
	
	DeleteQueryBuilder orWhere(String where);
	
	DeleteQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	void execute();

}
