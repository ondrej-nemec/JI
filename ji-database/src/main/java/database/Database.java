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
import querybuilder.DeleteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;
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
	
	public void applyQuery(final ConnectionConsumer consumer) throws SQLException {
		Connection con = DriverManager.getConnection(connectionString, createProperties());
		consumer.accept(con);
		con.close();
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
	
	/*** SEPARATOR ***/
	
	public abstract void startServer();
	
	public abstract void stopServer();
	
	protected abstract void createDb() throws SQLException;

	public abstract SelectQueryBuilder getSelectBuilder();
	
	public abstract UpdateQueryBuilder getUpdateBuilder();
	
	public abstract DeleteQueryBuilder getDeletetBuilder();
	
	public abstract InsertQueryBuilder getInsertBuilder();
	
	/*** SEPARATOR ***/
	
	public static Database getDatabase(final DatabaseConfig config, final Logger logger) {
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
	
	protected String createConnectionString() {
		return "jdbc:" + config.type + ":" + config.pathOrUrlToLocation + "/";
	}
	
	protected Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("create", "true");
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		
		return props;
	}
	
	protected String getConnectionString() {
		return connectionString;
	}
	
	private void migrate() throws FlywaySqlException {
		/*LogFactory.setLogCreator((clazz)->{
			return log;
		});*/
		Flyway f  = Flyway
				.configure()
				.dataSource(
					connectionString,
					config.login,
					config.password
				)
				.locations(config.pathToMigrations)
				.load();
		f.migrate();
	}
}
