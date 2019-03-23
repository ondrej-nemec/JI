package testing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import common.Env;
import database.Database;

public abstract class DatabaseTestCase {
	
	protected Connection con;
	
	private final Database database;
	
	protected final Env env;
	
	public DatabaseTestCase() {
		this.env = TestEnvFactory.createEnv();
		this.database = Database.getDatabase(env.createDbConfig());
		database.startServer();
	}
	
	@Before
	public void before() throws SQLException {
		database.createDbAndMigrate();
		con = database.getConnnection();
		con.setAutoCommit(false);
		applyDataSet();
	}
	
	@After
	public void after() throws SQLException {
		if (con != null) {
			con.rollback();
			con.close();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		database.stopServer();
		super.finalize();
	}
	
	private void applyDataSet() {
		Map<String, List<Map<String, String>>> dataSet = getDataSet();
		
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
		});
	}
	
	private String getBracketString(final String origin) {
		return "(" + origin.trim().replace(' ', ',') + ")";
	}
	
	protected abstract Map<String, List<Map<String, String>>> getDataSet();

}
