package testing;

import java.util.List;
import java.util.Properties;

import database.Database;
import testing.entities.Table;

public abstract class DatabaseTestCase extends TestCase {

	private final DatabaseMock database;
	
	public DatabaseTestCase(final String propertiesPath) {
		super(propertiesPath);
		this.database = new DatabaseMock(env.createDbConfig(), getDataSet());
	}
	
	public DatabaseTestCase(final Properties properties) {
		super(properties);
		this.database = new DatabaseMock(env.createDbConfig(), getDataSet());
	}
	
	public Database getDatabase() {
		return database;
	}
	
	public Database getNestedDatabase() {
		return database.getNestedDatabase();
	}
	
	protected abstract List<Table> getDataSet();

}
