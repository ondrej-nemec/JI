package migration;

import java.sql.Connection;
import java.sql.SQLException;

import common.Logger;
import common.functions.DateTime;
import common.functions.FileExtension;
import migration.migrations.JavaMigration;
import migration.migrations.SqlMigration;
import migration.resources.IdSeparator;
import querybuilder.QueryBuilder;

public class SingleMigrationTool {
	
	private final String allwaysId;
	
	private final String migrationTable;
	
	private final String separator;
	
	private final Logger logger;
	
	private final SqlMigration sql;
	
	private final JavaMigration java;
	
	private final String module;
		
	public SingleMigrationTool(
			String module, String migrationTable, String allwaysId, String separator,
			JavaMigration java, SqlMigration sql, Logger logger) {
		this.logger = logger;
		this.migrationTable = migrationTable;
		this.java = java;
		this.sql = sql;
		this.allwaysId = allwaysId;
		this.separator = separator;
		this.module = module;
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
			IdSeparator id = new IdSeparator(ex.getName(), separator);
			if (!id.getId().contains(allwaysId)) {
				idToDb(id.getId(), id.getDesc(), module, builder, isRevert);
			}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}
	}
	
	private void idToDb(String id, String description, String module, QueryBuilder builder, boolean isRevert) throws SQLException {
		if (isRevert) {
			logger.warn("Migration reverted: " + id);
			builder.delete(migrationTable)
				.where("id = :id").addParameter(":id", id)
				.andWhere("Module = :module").addParameter(":module", module)
				.execute();
		} else {
			builder
	    		.insert(migrationTable)
	    		.addValue("id", id)
	    		.addValue("Description", description)
	    		.addValue("DateTime", DateTime.format("YYYY-mm-dd H:m:s"))
	    		.addValue("Module", module)
	    		.execute();
		}
	}

}
