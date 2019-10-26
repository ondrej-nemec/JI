package migration;

import querybuilder.QueryBuilder;

public interface Migration {
	
	void migrate(QueryBuilder builder);
	
	void revert(QueryBuilder builder);

}
