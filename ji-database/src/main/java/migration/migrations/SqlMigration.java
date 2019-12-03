package migration.migrations;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Statement;

import querybuilder.QueryBuilder;
import text.BufferedReaderFactory;

public class SqlMigration implements SingleMigration {

	private final String path; // where migrations are
	
	private final boolean isRevert;
	
	private final boolean isInclasspath;
	
	public SqlMigration(String path, boolean isRevert, boolean isInclasspath) {
		this.path = path;
		this.isRevert = isRevert;
		this.isInclasspath = isInclasspath;
	}

	@Override
	public void migrate(String name, QueryBuilder builder) throws Exception {
		String[] batches = loadContent(path + "/" + name, isRevert, isInclasspath).split(";");
		Statement stat = builder.getConnection().createStatement();
		for (String batch : batches) {
			stat.addBatch(batch);
		}
		stat.executeBatch();
	}

	private String loadContent(String file, boolean isRevert, boolean isInclasspath) throws IOException {
		if (isInclasspath) {
			try (BufferedReader br = BufferedReaderFactory.buffer(getClass().getResourceAsStream("/" + file))) {
				return loadContent(br, isRevert, file);
			}
		} else {
			try (BufferedReader br = BufferedReaderFactory.buffer(file)) {
				return loadContent(br, isRevert, file);
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
