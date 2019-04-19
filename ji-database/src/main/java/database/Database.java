package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;

import org.flywaydb.core.internal.exception.FlywaySqlException;

import common.ILogger;
import utils.env.DatabaseConfig;

public abstract class Database {
	
	protected final DatabaseConfig config;
	
	protected final ILogger logger;
	
	private String connectionString;
	
	private Connection connection = null;

	public Database(final DatabaseConfig config, final ILogger logger) {
		this.config = config;
		this.logger = logger;
		this.connectionString = createConnectionString() + config.schemaName;
	}

	@Deprecated
	public Connection getConnnection() throws SQLException {
		if (this.connection == null)
			this.connection = DriverManager.getConnection(connectionString, createProperties());
		return connection;
	}
	
	@Deprecated
	public void stopConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}
	
	public void applyQuery(final Consumer<Connection> consumer) throws SQLException {
		Connection con = DriverManager.getConnection(connectionString, createProperties());
		consumer.accept(con);
		con.close();
	}
	
	public String getConnectionString() {
		return connectionString;
	}
	
	public boolean createDbAndMigrate() {
		try {
			// TODO check if this condition is nessesery
			if (config.runOnExternalServer) {
				DriverManager
					.getConnection(
							createConnectionString(),
							createProperties()
					).createStatement()
					.executeUpdate("CREATE DATABASE IF NOT EXISTS " + config.schemaName);
			} else {
				// create and close - create db schema
				getConnnection().close();		
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
	
	public static Database getDatabase(final DatabaseConfig config, final ILogger logger) {
		switch (config.type) {
		case "derby":
			return new Derby(config, logger);
		case "mysql":
			return new MySQL(config, logger);
		default:
			return null;
		}		
	}

	/*** SEPARATOR ***/
	
	private String createConnectionString() {
		return "jdbc:" + config.type + ":" + config.pathOrUrlToLocation + "/";
	}
	
	protected Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		
		return props;
	}
}
