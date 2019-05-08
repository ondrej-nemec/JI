package querybuilder;

public interface InsertQueryBuilder {
	
	InsertQueryBuilder addColumns(String... columns);
	
	InsertQueryBuilder values(String... values);
	
	InsertQueryBuilder addParameter(String name, String value);
	
	String getSql();
	
	void execute();

}
