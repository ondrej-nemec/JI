package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Properties;

import common.Logger;
import database.support.ConnectionFunction;
import database.support.DoubleConsumer;
import database.support.QueryBuilderFunction;
import database.support.SqlQueryProfiler;
import migration.MigrationTool;
import querybuilder.QueryBuilder;

public class Database {
	
	public static SqlQueryProfiler PROFILER = null;
	
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
			return new Derby(
					config.runOnExternalServer, 
					config.pathOrUrlToLocation, 
					createSchemaConnectionString(),
					createProperties(),
					logger
			);
		case "mysql":
			return new MySql(config.runOnExternalServer, createDatabaseConnectionString(), createProperties(), name, logger);
		case "postgresql":
			return new PosgreSql(config.runOnExternalServer, createDatabaseConnectionString(), createProperties(), name, logger);
		case "sqlserver":
			return new SqlServer(config.runOnExternalServer, createDatabaseConnectionString(), createProperties(), name, logger);
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
	
	public <T> T applyQuery(final ConnectionFunction<T> consumer) throws SQLException {
		return getDoubleFunction(consumer).get();
	}
	
	public <T> T applyBuilder(final QueryBuilderFunction<T> consumer) throws SQLException {
		return getDoubleFunction((con)->{
			return consumer.apply(getQueryBuilder(con));
		}).get();
	}

	/***************************/
	
	protected <T> DoubleConsumer<T> getDoubleFunction(ConnectionFunction<T> consumer) {
		return ()->{
			Connection con = pool.getConnection();
			try {
				con.setAutoCommit(false);
				T t = consumer.apply(con);
				con.commit();
				pool.returnConnection(con);
				return t;
			} catch (Exception e) {
				con.rollback();
				pool.returnConnection(con);
				throw new SQLException("Error in database consumer. Transaction rollback.", e);
			}
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
		props.setProperty("serverTimezone", ZoneId.systemDefault().toString());
		props.setProperty("create", "true");
		props.setProperty("allowMultiQueries", "true");
		return props;
	}
	
	private QueryBuilder getQueryBuilder(Connection connection) {
		return new QueryBuilder(connection, instance.getQueryBuilderFactory(connection));
	}
	
	/********* Migration ****************/
	
	public boolean createDbAndMigrate() {
		try {
			createDbIfNotExists();
			migrate();
			logger.info("All migrations were applied");
		} catch (SQLException e) {
			logger.fatal("Create db and migrante fail", e);
			return false;
		}
		return true;
	}
	
	public void createDbIfNotExists() throws SQLException {
		instance.createDb();
		logger.info("DB schema was created");
	}
	
	public void migrate() throws SQLException {
		applyBuilder((builder)->{
			MigrationTool tool = new MigrationTool(config.pathToMigrations, builder, logger);
			try {
				tool.migrate();
			} catch (Exception e) {
				throw new SQLException(e);
			}
			return null;
		});
	}
	
}
