package testing;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import database.Database;
import testing.entities.Table;

public abstract class DatabaseTestCase extends TestCase {
	
	private final DatabaseMock database;

	public DatabaseTestCase(final Properties properties) {
		super(properties);
		this.database = new DatabaseMock(env.createDbConfig(), getDataSet());
	}

	public DatabaseTestCase(final String propertiesPath) {
		super(propertiesPath);
		this.database = new DatabaseMock(env.createDbConfig(), getDataSet());
	}	

	protected abstract List<Table> getDataSet();
	
	@Before
	public void before() throws SQLException {
		database.createDbAndMigrate();
		database.prepare();
	}
	
	@After
	public void after() throws SQLException {
		database.clean();
	}
	
	protected Database getDatabase() {
		return database;
	}
	
	protected Database getNestedDatabase() {
		return database.getNestedDatabase();
	}
	
}