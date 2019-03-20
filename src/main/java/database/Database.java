package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.flywaydb.core.internal.exception.FlywaySqlException;

import common.DatabaseConfig;
import common.Terminal;
import logging.Logger;

public abstract class Database {

	private Connection con;
	
	protected final DatabaseConfig config;
	
	protected final Logger logger = Logger.getLogger(Database.class);
	
	private String connectionString;
	
	public Database(final DatabaseConfig config) {
		this.config = config;
		this.connectionString = createConnectionString() + config.schemaName;
	}
	
	public Connection getConnnection() throws SQLException {
		return DriverManager.getConnection(connectionString, createProperties());
	}
	
	public void closeConnection() throws SQLException {
		if(con != null)
			con.close();
	}
	
	public String getConnectionString() {
		return connectionString;
	}
	
	public boolean createDbAndMigrate() {
		try {
			if (config.runOnExternalServer) {
				DriverManager
					.getConnection(
							createConnectionString(),
							createProperties()
					).createStatement()
					.executeUpdate("CREATE DATABASE IF NOT EXISTS " + config.schemaName);
			} else {
				// create and close - create db schema
				getConnnection();
				closeConnection();			
			}
			logger.info("DB schema was created");
			new Migrate(
					getConnectionString(),
					config.login,
					config.password,
					config.pathToMigrations
			);
			logger.info("All migrations were applied");
		} catch (SQLException | FlywaySqlException e) {
			logger.fatal("Create db and migrante fail", e);
			return false;
		}
		return true;
	}
	
	/*** SEPARATOR ***/
	
	public abstract void startServer();
	
	public abstract void stopServer();
	
	/*** SEPARATOR ***/
	
	public static Database getDatabase(final DatabaseConfig config, final Terminal terminal) {
		switch (config.type) {
		case "derby":
			return new Derby(config, terminal);
		case "mysql":
			return new MySQL(config);
		default:
			return null;
		}		
	}
	
	/*** SEPARATOR ***/
	
	private String createConnectionString() {
		return "jdbc:" + config.type + ":" + config.pathOrUrlToLocation + "/";
	}
	
	private Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		
		return props;
	}
}
