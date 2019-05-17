package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import testing.entities.Row;
import testing.entities.Table;

public class DatabaseTestCaseTest extends DatabaseTestCase {

	public DatabaseTestCaseTest() {
		super(getProperties());
	}
	
	@Before
	@Override
	public void before() throws SQLException {
		testDbEmptyOrNotExists();
		super.before();
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
			getNestedDatabase().applyQuery((con)->{
				testDbEmpty();
			});
		} catch (SQLException | RuntimeException e) {
			assertEquals("Unknown database 'javainit_testing_test'", e.getMessage());
		}
	}
	
	private void testDbEmpty() throws SQLException {
		getNestedDatabase().applyQuery((con)->{
			PreparedStatement stat = con.prepareStatement("select * from dbtc");
			ResultSet res = stat.executeQuery();
			assertFalse(res.next());
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
		prop.put("db.type", "mysql");
		prop.put("db.pathOrUrl", "//localhost:3306");
		prop.put("db.externalServer", "1");
		prop.put("db.schema", "javainit_testing_test");
		prop.put("db.login", "root");
		prop.put("db.password", "");
		prop.put("db.pathToMigrations", "testing");
		
		prop.put("log.logFile", "log.txt");
		prop.put("log.type", "null");
		return prop;
	}

}
