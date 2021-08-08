package testing;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import common.Logger;
import common.functions.Env;
import database.Database;
import database.DatabaseConfig;
import testing.entities.Table;

public abstract class DatabaseTestCase {
	
	protected final DatabaseMock database;
	protected final DatabaseConfig config;

	public DatabaseTestCase(DatabaseConfig config, Logger logger) {
		this.config = config;
		this.database = new DatabaseMock(
				config,
				getDataSet(),
				logger
		);
	}

	public DatabaseTestCase(Env env, Logger logger) {
		this(new DatabaseConfig(
				env.getString("db.type"),
				env.getString("db.pathOrUrl"),
				env.getBoolean("db.externalServer"),
				env.getString("db.schema"),
				env.getString("db.login"),
				env.getString("db.password"),
				env.getList("db.pathToMigrations", ","),
				env.getInteger("db.poolSize")
		), logger);
	}

	protected abstract List<Table> getDataSet();
	
	@Before
	public void before() throws SQLException {
		database.migrate();
		applyDataSet();
	}
	
	@After
	public void after() throws SQLException {
		database.rollback();
	}
	
	protected Database getDatabase() {
		return database;
	}

	/**
	 * protected for test only
	 * using without REALLY good reason could make troubles
	 * @throws SQLException 
	 */
	protected void applyDataSet() throws SQLException {
		database.applyDataSet();
	}

}