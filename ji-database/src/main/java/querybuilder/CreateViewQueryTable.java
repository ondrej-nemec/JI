package querybuilder;

public interface CreateViewQueryTable {
	
	CreateViewQueryTable select(String param);

	CreateViewQueryTable from(String table);
	
	CreateViewQueryTable join(String table, Join join, String on);

	CreateViewQueryTable where(String where);
	
	CreateViewQueryTable andWhere(String where);
	
	CreateViewQueryTable orWhere(String where);
	
	CreateViewQueryTable orderBy(String orderBy);
	
	CreateViewQueryTable groupBy(String groupBy);
	
	CreateViewQueryTable having(String having);
	
	CreateViewQueryTable limit(int limit);
	
	CreateViewQueryTable offset(int offset);
	
	void execute();
	
}
