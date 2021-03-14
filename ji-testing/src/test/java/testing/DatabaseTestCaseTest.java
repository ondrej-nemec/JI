package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import common.Logger;
import common.functions.Env;
import database.Database;
import testing.entities.Row;
import testing.entities.Table;

public class DatabaseTestCaseTest extends DatabaseTestCase {

	private final Database realDatabase;
	
	public DatabaseTestCaseTest() {
		super(new Env(getProperties()), mock(Logger.class));
		this.realDatabase = new Database(config, mock(Logger.class));
	}
	
	@Before
	@Override
	public void before() throws SQLException {
		getDatabase().migrate();
		testDbEmptyOrNotExists();
		applyDataSet();
	}
	
	@After
	@Override
	public void after() throws SQLException {
		super.after();
		testDbEmpty();
	}	
	
	@Test
	public void testDataInDb() throws SQLException {
		getDatabase().applyQuery((con)->{
			PreparedStatement stat = con.prepareStatement("select * from dbtc");
			ResultSet res = stat.executeQuery();
			
			for (int i = 0; i < 3; i++) {
				assertTrue(res.next());
				assertEquals(i, res.getInt(1));
				assertEquals("Name #" + i, res.getObject(2));
			}
			return null;
		});
	}
	
	@Test
	@Ignore
	public void testThrowingTest() throws IOException {
		throw new IOException("Expected exception");
	}
	
	@Test(expected=IOException.class)
	public void testWithExpectedException() throws IOException {
		throw new IOException("Expected exception");
	}

	@Override
	protected List<Table> getDataSet() {			
		return Arrays.asList(
			new Table(
				"dbtc",
				Arrays.asList(
					getRow(0),
					getRow(1),
					getRow(2)
				)
			)
		);
	}
	
	private void testDbEmptyOrNotExists() {
		try {
			realDatabase.applyQuery((con)->{
				testDbEmpty();
				return null;
			});
		} catch (SQLException | RuntimeException e) {
			assertEquals("Unknown database 'javainit_testing_test'", e.getMessage());
		}
	}
	
	private void testDbEmpty() throws SQLException {
		realDatabase.applyQuery((con)->{
			try {
				PreparedStatement stat = con.prepareStatement("select * from dbtc");
				ResultSet res = stat.executeQuery();
				assertFalse(res.next());
			} catch (SQLException e) {
				assertTrue(e.getMessage().contains("ERROR: relation \"dbtc\" does not exist"));
			}
			return null;
		});
	}
	
	private Row getRow(int i) {
		Row row = new Row();
		row.addColumn("id", i);
		row.addColumn("name", "Name #" + i);
		return row;
	}	
	
	private static Properties getProperties() {
		Properties prop = new Properties();
		prop.put("app.mode", "test");
		prop.put("db.timezone", "Europe/Prague");
		
		prop.put("db.type", "postgresql");
		prop.put("db.pathOrUrl", "//localhost");
		prop.put("db.externalServer", true);
		prop.put("db.schema", "javainit_testing_test");
		prop.put("db.login", "postgres");
		prop.put("db.password", "1234");
		prop.put("db.pathToMigrations", "migrations");
		prop.put("db.poolSize", 3);
		/*
		prop.put("db.type", "mysql");
		prop.put("db.pathOrUrl", "//localhost:3306");
		prop.put("db.externalServer", true);
		prop.put("db.schema", "javainit_testing_test");
		prop.put("db.login", "root");
		prop.put("db.password", "");
		prop.put("db.pathToMigrations", "testing");
		prop.put("db.poolSize", 3);
		*/
		prop.put("log.logFile", "log.txt");
		prop.put("log.type", "null");
		return prop;
	}

}
