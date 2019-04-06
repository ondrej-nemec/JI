package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import testing.entities.Row;
import testing.entities.Table;

public class DatabaseTestCaseTest {
	
	class DbtcImpl extends DatabaseTestCase {
		
		public DbtcImpl(Properties prop) {
			super(prop);
		}
			
		@Test
		public void testDataInDb() throws SQLException {
			getDatabase().applyQuery((con)->{
				try {
					PreparedStatement stat = con.prepareStatement("select * from dbtc");
					ResultSet res = stat.executeQuery();
					
					for (int i = 0; i < 3; i++) {
						assertTrue(res.next());
						assertEquals(i, res.getInt(1));
						assertEquals("Name #" + i, res.getObject(2));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			});
		}
		
		@Test
		public void testThrowingTest() {
			// TODO throw
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
		
		private Row getRow(int i) {
			Row row = new Row();
			row.addColumn("id", i);
			row.addColumn("name", "Name #" + i);
			return row;
		}
	}
	
	//TODO not a test - run via main - without db maven don't compile this

	@Test
	public void testDbtcInsertDataAndAfterTestClearDb() throws SQLException {
		Properties prop = new Properties();
		prop.put("db.type", "mysql");
		prop.put("db.pathOrUrl", "//localhost:3306");
		prop.put("db.externalServer", "1");
		prop.put("db.schema", "env_test");
		prop.put("db.login", "root");
		prop.put("db.password", "");
		prop.put("db.pathToMigrations", "testing");	
		
		DbtcImpl dbtc = new DbtcImpl(prop);
		
		testDbEmptyOrNotExists(dbtc);
		
		dbtc.testDataInDb();
		dbtc.testThrowingTest();
		
		testDbEmpty(dbtc);
		
	}
	
	private void testDbEmptyOrNotExists(DatabaseTestCase dbtc) {
		try {
			dbtc.getNestedDatabase().applyQuery((con)->{
				try {
					PreparedStatement stat = con.prepareStatement("select * from dbtc");
					ResultSet res = stat.executeQuery();
					assertFalse(res.next());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			});
		} catch (SQLException e) {
			assertEquals("Unknown database 'env_test'", e.getMessage());
		}
		
	}
	
	private void testDbEmpty(DatabaseTestCase dbtc) throws SQLException {
		dbtc.getNestedDatabase().applyQuery((con)->{
			try {
				PreparedStatement stat = con.prepareStatement("select * from dbtc");
				ResultSet res = stat.executeQuery();
				assertFalse(res.next());
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
}
