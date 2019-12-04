package migration;

import java.sql.Connection;
import java.sql.SQLException;

import common.DateTime;
import common.FileExtension;
import common.Logger;
import migration.migrations.JavaMigration;
import migration.migrations.SqlMigration;
import migration.resources.IdSeparator;
import querybuilder.QueryBuilder;

public class SingleMigrationTool {
	
	private final static String ALLWAIS_ID = "ALLWAYS";
	
	private final String migrationTable = "migrations";
	
	private final Logger logger;
	
	private final SqlMigration sql;
	
	private final JavaMigration java;
	
	public SingleMigrationTool(JavaMigration java, SqlMigration sql, Logger logger) {
		this.logger = logger;
		this.java = java;
		this.sql = sql;
	}

	public void transaction(String file, QueryBuilder builder, boolean isRevert) throws Exception {
		Connection con = builder.getConnection();
		try {
			con.setAutoCommit(false);
			FileExtension ex = new FileExtension(file);
			switch (ex.getExtension()) {
			case "sql":
				sql.migrate(ex.getName() + ".sql", builder);
				break;
			case "class":
			case "java":
				java.migrate(ex.getName(), builder);
				break;
			}
			IdSeparator id = new IdSeparator(ex.getName());
			if (!id.getId().contains(ALLWAIS_ID)) {
				idToDb(id.getId(), id.getDesc(), builder, isRevert);
			}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}
	}
	
	private void idToDb(String id, String description, QueryBuilder builder, boolean isRevert) throws SQLException {
		if (isRevert) {
			logger.warn("Migration reverted: " + id);
			builder.delete(migrationTable).where("id = :id").addParameter("id", id).execute();
		} else {
			builder
    		.insert(migrationTable)
    		.addValue("id", id)
    		.addValue("Description", description)
    		.addValue("DateTime", DateTime.format("YYYY-mm-dd H:m:s"))
    		.execute();
		}
	}

}
