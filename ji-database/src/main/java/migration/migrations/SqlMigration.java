package migration.migrations;

import java.io.BufferedReader;
import java.io.IOException;

import querybuilder.QueryBuilder;
import text.BufferedReaderFactory;

public class SqlMigration implements SingleMigration {

	private final String path; // where migrations are
	
	private final boolean isRevert;
	
	private final boolean isInclasspath;
	
	public SqlMigration(String path, boolean isRevert, boolean isInclasspath) {
		super();
		this.path = path;
		this.isRevert = isRevert;
		this.isInclasspath = isInclasspath;
	}

	@Override
	public void migrate(String name, QueryBuilder builder) throws Exception {
	/*	Statement stat = builder.getConnection().createStatement();
		String[] batches = loadContent(name, revert, inClasspath).split(";");
		for (String batch : batches) {
			stat.addBatch(batch);
		}
		stat.executeBatch();*/
	}
	
	/**
	 * Load content of sql file depending on @param revert
	 */
	private String loadContent(String file, boolean revert, boolean external) throws IOException {
		if (external) {
			try (BufferedReader br = BufferedReaderFactory.buffer(file)) {
				return loadContent(br, revert, file);
			}
		} else {
			try (BufferedReader br = BufferedReaderFactory.buffer(getClass().getResourceAsStream("/" + file))) {
				return loadContent(br, revert, file);
			}
		}
	}
	
	private String loadContent(BufferedReader br, boolean revert, String file) throws IOException {
		StringBuilder sql = new StringBuilder();
		String line = br.readLine();
		while (line != null) {
			sql.append(line);
			line = br.readLine();
		}
		
		String[] mig = sql.toString().split("--- REVERT ---");
		if (revert && mig.length > 1) {
			return mig[1];
		} else if (revert && mig.length < 2) {
			throw new RuntimeException(String.format("Migration %s has not revert part", file));
		} else if (!revert && mig.length == 1) {
			return sql.toString();
		} else if (!revert && mig.length > 1) {
			return mig[0];
		}
		return sql.toString();
	}
}
