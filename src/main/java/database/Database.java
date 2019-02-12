package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.flywaydb.core.internal.exception.FlywaySqlException;

import common.DatabaseConfig;
import common.Terminal;

public abstract class Database {

	private Connection con;
	
	protected final DatabaseConfig config;
	
	protected final Logger logger;
	
	private String connectionString;
	
	public Database(final DatabaseConfig config, final Logger logger) {
		this.config = config;
		this.logger = logger;
		this.connectionString = "jdbc:" + config.type + ":"
				+ (config.runOnExternalServer ? "//" : "")
				+ config.pathOrUrlToLocation + "/" + config.schemaName;
	}
	
	public Connection getConnnection() throws SQLException {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		
		return DriverManager.getConnection(connectionString, props);
	}
	
	public void closeConnection() throws SQLException {
		if(con != null)
			con.close();
	}
	
	public String getConnectionString() {
		return this.connectionString;
	}
	
	public void createDbAndMigrate() throws SQLException, FlywaySqlException {
		// create and close - create db schema
		getConnnection();
		closeConnection();
		logger.info("DB schema was created");
		new Migrate(
				getConnectionString(),
				config.login,
				config.password,
				config.pathToMigrations
		);
		logger.info("All migrations were applied");
	}
	
	public abstract void startServer();
	
	public abstract void stopServer();
	
	public static Database getDatabase(final DatabaseConfig config, final Logger logger, final Terminal terminal) {
		switch (config.type) {
		case "derby":
			return new Derby(config, logger, terminal);
		case "mysql":
			return new MySQL(config, logger);
		default:
			return null;
		}		
	}

}
