package migration.migrations;

import querybuilder.QueryBuilder;

public class JavaMigration implements SingleMigration {
	
	private final String path; // where migrations are
	
	private final ClassLoader loader;
	
	private final boolean isRevert;
	
	private final boolean isInclasspath;

	public JavaMigration(String path, ClassLoader loader, boolean isRevert, boolean isInclasspath) {
		this.path = path;
		this.loader = loader;
		this.isRevert = isRevert;
		this.isInclasspath = isInclasspath;
	}

	@Override
	public void migrate(String name, QueryBuilder builder) throws Exception {
		/*
		path = inClasspath ? path + "." :  ""
		
		Migration m = (Migration)loader.loadClass(path + name).newInstance();
		if (revert) {
			m.revert(builder);
		} else {
			m.migrate(builder);
		}
		logger.debug("Migration applied " + path + name);
		*/
	}
	
}
