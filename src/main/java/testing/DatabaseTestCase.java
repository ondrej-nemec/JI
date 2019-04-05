package testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import database.Database;
import exceptions.NotImplementedYet;
import testing.entities.Row;
import testing.entities.Table;

public abstract class DatabaseTestCase extends TestCase {
	
	private Connection con;
	
	protected final Database database;
	
	public DatabaseTestCase(final String propertiesPath) {
		super(propertiesPath);
		this.database = Database.getDatabase(env.createDbConfig());
		database.startServer();
	}
	
	public DatabaseTestCase(final Properties properties) {
		super(properties);
		this.database = Database.getDatabase(env.createDbConfig());
		database.startServer();
	}
	
	//TODO
	@Before
	public void before() throws SQLException {
		database.createDbAndMigrate();
	/*	con = database.getConnnection();
	//	con.setAutoCommit(false);*/
		applyDataSet();
	}

	//TODO
	@After
	public void after() throws SQLException {
		/*if (con != null) {
	//		con.rollback();
			con.close();
		}*/
	}
	
	@Override
	protected void finalize() throws Throwable {
		/*database.stopServer();
		super.finalize();*/
	}

	//TODO
	private void applyDataSet() {
		throw new NotImplementedYet();
		/*Map<String, List<Map<String, String>>> dataSet =  null; // getDataSet();
		
		if (dataSet == null)
			return;
		
		dataSet.forEach((table, rows)->{			
			for(Map<String, String> row : rows) {
				String names = "";
				String values = "";
				for (String columnName : row.keySet()) {
					names += columnName + " ";
					values += "'" + row.get(columnName) + "' ";
				}
				
				try {
					Statement st = con.createStatement();
					st.executeUpdate(
							"INSERT INTO " + table + " " + getBracketString(names) + " VALUES " + getBracketString(values)	
					);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}	
		});*/
	}
	
	private String getBracketString(final String origin) {
		return "(" + origin.trim().replace(' ', ',') + ")";
	}
	
	protected abstract List<Table> getDataSet();

}
