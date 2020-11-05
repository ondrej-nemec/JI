package migration.migrations;

import java.io.IOException;
import java.sql.Statement;

import core.text.InputStreamLoader;
import core.text.Text;
import core.text.basic.ReadText;
import querybuilder.QueryBuilder;

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
			if (!batch.trim().isEmpty()) {
				stat.addBatch(batch.trim());
			}
		}
		stat.executeBatch();
	}

	private String loadContent(String file, boolean isRevert, boolean isInclasspath) throws IOException {
		String sql = Text.read((br)->{
			return ReadText.asString(br);
		}, InputStreamLoader.createInputStream(getClass(), file));
		
		String[] mig = sql.split("--- REVERT ---");
		if (isRevert && mig.length > 1) {
			return mig[1];
		} else if (isRevert && mig.length < 2) {
			throw new RuntimeException(String.format("Migration %s has not revert part", file));
		} else if (!isRevert && mig.length == 1) {
			return sql.toString();
		} else if (!isRevert && mig.length > 1) {
			return mig[0];
		}
		return sql.toString();
	}
}
