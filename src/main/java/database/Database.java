package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import common.Env;

public abstract class Database {

	private Connection con;
	
	protected final Env env;
	
	protected final Logger logger;
	
	public Database(Env env, Logger logger) {
		this.env = env;
		this.logger = logger;
	}
	
	public Connection getConnnection() throws SQLException {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", env.databaseLogin);
		props.setProperty("password", env.databasePassword);
		
		String connectionString = "jdbc:" + env.databaseType + ":" + env.databaseLocation +"/" + env.databaseName;
		return DriverManager.getConnection(connectionString, props);
	}
	
	public void stopConnection() throws SQLException {
		if(con != null)
			con.close();
	}
	
	public abstract void startServer();
	
	public abstract void stopServer();

}
