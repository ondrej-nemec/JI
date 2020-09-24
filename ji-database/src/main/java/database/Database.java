package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.logging.LogFactory;
import org.flywaydb.core.internal.exception.FlywaySqlException;

import common.Logger;
import database.support.ConnectionConsumer;
import database.support.DoubleConsumer;
import database.support.FlywayLogger;
import database.support.QueryBuilderConsumer;

public class Database {
	
	private final DatabaseConfig config;
	
	private final Logger logger;
	
	protected final DatabaseInstance instance;
	
	protected final ConnectionPool pool;

	public Database(DatabaseConfig config, Logger logger) {
		this(config, false, logger);
	}

	protected Database(DatabaseConfig config, boolean isTemp, Logger logger) {
		this.config = config;
		this.logger = logger;
		this.pool = new ConnectionPool(createSchemaConnectionString(), createProperties(), config.poolSize, logger, isTemp);
		this.instance = createInstance(config.schemaName, logger);
	}

	protected Database(DatabaseConfig config, DatabaseInstance instance, ConnectionPool pool, Logger logger) {
		this.config = config;
		this.logger = logger;
		this.instance = instance;
		this.pool = pool;
	}
	
	private DatabaseInstance createInstance(String name, Logger logger) {
		switch (config.type) {
		case "derby":
			return new Derby(config.pathOrUrlToLocation, getDoubleConsumer(), logger);
		case "mysql":
			return new MySql(createDatabaseConnectionString(), createProperties(), name, logger);
		case "posgresql":
			return new PosgreSql(createDatabaseConnectionString(), createProperties(), name);
		default:
			throw new RuntimeException("Unsupported type " + config.type);
		}
	}
	
	/************ API ***********/
	
	public void startServer() {
		instance.startServer();
	}
	
	public void stopServer() {
		instance.stopServer();
	}
	
	public void applyQuery(final ConnectionConsumer consumer) throws SQLException {
		getDoubleConsumer().accept(consumer);
	}
	
	public void applyBuilder(final QueryBuilderConsumer consumer) throws SQLException {
		getDoubleConsumer().accept((con)->{
			consumer.accept(instance.getQueryBuilder(con));
		});
	}

	/***************************/
	
	protected DoubleConsumer getDoubleConsumer() {
		return (consumer)->{
			Connection con = pool.getConnection();
			consumer.accept(con);
			pool.returnConnection(con);
		};
	}
	
	/********* CONNECTION STRING **********/
	
	private String createDatabaseConnectionString() {
		return "jdbc:" + config.type + ":" + config.pathOrUrlToLocation + "/";
	}
	
	private String createSchemaConnectionString() {
		return createDatabaseConnectionString() + config.schemaName;
	}
	
	private Properties createProperties() {
		Properties props = new Properties();
		props.setProperty("user", config.login);
		props.setProperty("password", config.password);
		props.setProperty("serverTimezone", config.timezone);
		props.setProperty("create", "true");
		props.setProperty("allowMultiQueries", "true");
		return props;
	}
	
	// for flyway migrations
	private String createFullConnectionString() {
		String con = createSchemaConnectionString();
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
	
	/********* Migration ****************/
	
	public boolean createDbAndMigrate() {
		try {
			createDbIfNotExists();
			migrate();
			logger.info("All migrations were applied");
		} catch (SQLException | FlywaySqlException e) {
			logger.fatal("Create db and migrante fail", e);
			return false;
		}
		return true;
	}
	
	public void createDbIfNotExists() throws SQLException {
		instance.createDb();
		logger.info("DB schema was created");
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
