package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import common.Env;
import common.Terminal;

public abstract class Database {

	private Connection con;
	
	protected final Env env;
	
	protected final Logger logger;
	
	private String connectionString;
	
	public Database(Env env, Logger logger) {
		this.env = env;
		this.logger = logger;
		this.connectionString = "jdbc:" + env.databaseType + ":" + env.databaseLocation +"/" + env.databaseName;
	}
	
	public Connection getConnnection() throws SQLException {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", env.databaseLogin);
		props.setProperty("password", env.databasePassword);
		
		return DriverManager.getConnection(connectionString, props);
	}
	
	public void closeConnection() throws SQLException {
		if(con != null)
			con.close();
	}
	
	public String getConnectionString() {
		return this.connectionString;
	}
	
	public abstract void startServer();
	
	public abstract void stopServer();
	
	public static Database getDatabase(Env env, Logger logger, Terminal terminal) {
		switch (env.databaseType) {
		case "derby":
			return new Derby(env, logger, terminal, env.pathToAppWorkspace);
		case "mysql":
			return new MySQL(env, logger);
		default:
			return null;
		}		
	}

}
