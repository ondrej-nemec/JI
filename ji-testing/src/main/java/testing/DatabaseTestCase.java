package testing;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.Before;

import database.Database;
import logging.LoggerFactory;
import testing.entities.Table;

public abstract class DatabaseTestCase extends TestCase {

	private final DatabaseMock database;

	public DatabaseTestCase(final Properties properties) {
		super(properties);
		this.database = new DatabaseMock(
				env.createDbConfig(),
				getDataSet(),
				new LoggerFactory(env.createLogConfig()).getLogger(DatabaseMock.class)
		);
	}

	public DatabaseTestCase(final String propertiesPath) {
		super(propertiesPath);
		this.database = new DatabaseMock(
				env.createDbConfig(),
				getDataSet(), 
				new LoggerFactory(env.createLogConfig()).getLogger(DatabaseMock.class)
		);
	}	

	protected abstract List<Table> getDataSet();
	
	@Before
	public void before() throws SQLException {
		database.createDbAndMigrate();
		applyDataSet();
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