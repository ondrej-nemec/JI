package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.logging.LogFactory;
import org.flywaydb.core.internal.exception.FlywaySqlException;

import common.Logger;
import database.support.ConnectionConsumer;
import database.support.DoubleConsumer;
import database.support.FlywayLogger;
import querybuilder.QueryBuilder;
import utils.env.DatabaseConfig;

public abstract class Database {
	
	protected final DatabaseConfig config;
	
	protected final Logger logger;
	
	private String connectionString;

	public Database(final DatabaseConfig config, final Logger logger) {
		this.config = config;
		this.logger = logger;
		this.connectionString = createConnectionString() + config.schemaName;
	}
	
	public boolean createDbAndMigrate() {
		try {
			createDb();
			logger.info("DB schema was created");

			migrate();
			logger.info("All migrations were applied");
		} catch (SQLException | FlywaySqlException e) {
			logger.fatal("Create db and migrante fail", e);
			return false;
		}
		return true;
	}
	
	public void applyQuery(final ConnectionConsumer consumer) throws SQLException {
		getDoubleConsumer().accept(consumer);
	}

	public QueryBuilder getQueryBuilder() {
		return getQueryBuilder(getDoubleConsumer());
	}
	
	protected DoubleConsumer getDoubleConsumer() {
		return (consumer)->{
			Connection con = DriverManager.getConnection(connectionString, createProperties());
			consumer.accept(con);
			con.close();
		};
	}
	
	/*** SEPARATOR ***/
	
	public abstract void startServer();
	
	public abstract void stopServer();

	/**
	 * DO NOT USE public for DatabaseTestCase only
	 * @param consumer
	 * @return
	 */
	public abstract QueryBuilder getQueryBuilder(DoubleConsumer consumer);
	
	protected abstract void createDb() throws SQLException;
	
	/*** SEPARATOR ***/
	
	public static Database getDatabase(final DatabaseConfig config, final Logger logger) {
		switch (config.type) {
		case "derby":
			return new Derby(config, logger);
		case "mysql":
			return new MySql(config, logger);
		default:
			return null;
		}		
	}

	/*** SEPARATOR ***/
	
	protected String createConnectionString() {
		return "jdbc:" + config.type + ":" + config.pathOrUrlToLocation + "/";
	}
	
	protected Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		props.setProperty("serverTimezone", config.timezone);

		return props;
	}
	
	protected String getConnectionString() {
		return connectionString;
	}
	
	private String createFullConnectionString() {
		String con = connectionString;
		Properties p = createProperties();
		boolean first = true;
		for(Object property : p.keySet()) {
			if (first) {
				first = false;
				con += "?";
			} else {
				con += "&";
			}
			con += property +"=" + p.getProperty(property.toString());
		}
		
		return con;
	}
	
	private void migrate() throws FlywaySqlException {
		LogFactory.setLogCreator((clazz)->{
			return new FlywayLogger(this.logger);
		});
		Flyway f  = Flyway
				.configure()
				.dataSource(
					createFullConnectionString(), // connectionString,
					config.login,
					config.password
				)
				.locations(config.pathToMigrations)
				.load();
		f.migrate();
	}
}
