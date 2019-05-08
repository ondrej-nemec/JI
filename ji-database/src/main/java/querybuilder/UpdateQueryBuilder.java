package querybuilder;

public interface UpdateQueryBuilder {
	
	UpdateQueryBuilder set(String update);
	
	UpdateQueryBuilder where(String where);
	
	UpdateQueryBuilder andWhere(String where);
	
	UpdateQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	void execute();
}
