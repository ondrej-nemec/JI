package ji.testing;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import ji.common.Logger;
import ji.common.functions.Env;
import ji.database.Database;
import ji.database.DatabaseConfig;
import ji.testing.entities.Table;

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
                env.getString("database.type"),
                env.getString("database.url"),
                env.getBoolean("database.external") == null ? true : env.getBoolean("database.external"),
                env.getString("database.schema-name"),
                env.getString("database.login"),
                env.getString("database.password"),
                 env.getList("database.pathToMigrations", ","),
                env.getInteger("database.pool-size")
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