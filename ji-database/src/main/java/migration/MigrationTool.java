package migration;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import common.Logger;
import migration.migrations.JavaMigration;
import migration.migrations.SqlMigration;
import migration.resources.IdSeparator;
import migration.resources.MigrationFiles;
import migration.resources.MigrationPreparation;
import querybuilder.QueryBuilder;

public class MigrationTool {
	
	private final static String MIGRATION_TABLE = "migrations";
	private final static String ALLWAYS_ID = "ALLWAYS";
	private static final String SEPARATOR = "__";
	
	private final Logger logger;
	private final QueryBuilder builder;
	private final String folder;
	
	public MigrationTool(String folder, QueryBuilder builder, Logger logger) {
		this.logger = logger;
		this.folder = folder;
		this.builder = builder;
	}

	public void migrate() throws Exception {
		process(folder, false, builder, (filesToMigrate, single, builder)->{
			for (int i = 0; i < filesToMigrate.size(); i++) {
    			single.transaction(filesToMigrate.get(i), builder, false);
    		}
		});
	}
	
	public void revert() throws Exception {
		process(folder, true, builder, (filesToMigrate, single, builder)->{
			for (int i = filesToMigrate.size(); i > 0; i--) {
				single.transaction(filesToMigrate.get(i-1), builder, true);
			}
		});
	}
	
	public void revert(String id) throws Exception {
		process(folder, true, builder, (filesToMigrate, single, builder)->{
			for (int i = filesToMigrate.size(); i > 0; i--) {
    			if (new IdSeparator(filesToMigrate.get(i-1), SEPARATOR).getId().equals(id)) {
    				return;
    			}
    			single.transaction(filesToMigrate.get(i-1), builder, true);
    		}
		});
	}
	
	public void revert(int steps) throws Exception {
		process(folder, true, builder, (filesToMigrate, single, builder)->{
			for (int i = filesToMigrate.size(); filesToMigrate.size() - i < steps; i--) {
    			single.transaction(filesToMigrate.get(i-1), builder, true);
    		}
		});
	}

	private void process(String folder, boolean isRevert, QueryBuilder builder, MigrationProcess process) throws Exception {
		MigrationFiles files = new MigrationFiles(folder);
		MigrationPreparation prep = new MigrationPreparation(MIGRATION_TABLE, SEPARATOR);
		List<String> filesToMigrate = prep.getFilesToMigrate(files.getFiles(), isRevert, builder);
		URL[] urls = new URL[]{files.getDir().toURI().toURL()};
		
		// each migration
		try (URLClassLoader loader = new URLClassLoader(urls);) {
    		SqlMigration sql = new SqlMigration(folder, isRevert, files.isInClasspath());
    		JavaMigration java = new JavaMigration(folder, loader, isRevert, files.isInClasspath());
			SingleMigrationTool tool = new SingleMigrationTool(MIGRATION_TABLE, ALLWAYS_ID, SEPARATOR, java, sql, logger);
    		process.process(filesToMigrate, tool, builder);
    	}
		
	}

}
