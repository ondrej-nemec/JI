package migration;

import java.util.List;

import querybuilder.QueryBuilder;

public interface MigrationProcess {

	void process(List<String> filesToMigrate, SingleMigrationTool tool, QueryBuilder builder) throws Exception;
	
}
