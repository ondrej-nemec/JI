package migration.migrations;

import querybuilder.QueryBuilder;

public interface SingleMigration {
	
	void migrate(String name, QueryBuilder builder) throws Exception;

}
