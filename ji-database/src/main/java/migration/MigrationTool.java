package migration;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
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
	
	public MigrationTool(Logger logger) {
		this.logger = logger;
	}

	public void migrate() throws Exception {
	/*	File file = getMigrationDir(folder);
		Map<String, String> map = loadFiles(file.listFiles());
		doMigrations(file, map, folder, false);*/
	}
	
	public void revert() {
		
	}
	
	public void revert(String id) {
		
	}
	
	public void revert(int steps) {
		
	}
	
	private void process(String folder, boolean isRevert, QueryBuilder builder) throws IOException, SQLException, MigrationException {
		MigrationFiles files = new MigrationFiles(folder);
		MigrationPreparation prep = new MigrationPreparation(MIGRATION_TABLE, SEPARATOR);
		List<String> filesToMigrate = prep.getFilesToMigrate(files.getFiles(), isRevert, builder);
		URL[] urls = new URL[]{files.getDir().toURI().toURL()};
		
		// each migration
		try (URLClassLoader loader = new URLClassLoader(urls);) {
    		SqlMigration sql = new SqlMigration(folder, isRevert, files.isInClasspath());
    		JavaMigration java = new JavaMigration(folder, loader, isRevert, files.isInClasspath());
			SingleMigrationTool tool = new SingleMigrationTool(MIGRATION_TABLE, ALLWAYS_ID, SEPARATOR, java, sql, logger);
    		
    	}
		
	}
	
	private void foward(List<String> filesToMigrate, SingleMigrationTool single, QueryBuilder builder) throws Exception {
		for (int i = 0; i < filesToMigrate.size(); i++) {
			single.transaction(filesToMigrate.get(i), builder, false);
		}
	}
	
	private void back(List<String> filesToMigrate, SingleMigrationTool single, QueryBuilder builder, int steps) throws Exception {
		for (int i = filesToMigrate.size(); filesToMigrate.size() - i < steps; i--) {
			single.transaction(filesToMigrate.get(i-1), builder, true);
		}
	}
	
	private void back(List<String> filesToMigrate, SingleMigrationTool single, QueryBuilder builder, String id) throws Exception {
		for (int i = filesToMigrate.size(); i > 0; i--) {
			if (new IdSeparator(filesToMigrate.get(i-1), SEPARATOR).getId().equals(id)) {
				return;
			}
			single.transaction(filesToMigrate.get(i-1), builder, true);
		}
	}
}
