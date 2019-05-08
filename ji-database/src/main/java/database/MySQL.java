package database;

import java.sql.DriverManager;
import java.sql.SQLException;

import common.Logger;
import database.mysql.MySqlQueryBuilder;
import querybuilder.QueryBuilder;
import utils.env.DatabaseConfig;

public class MySQL extends Database {

	public MySQL(final DatabaseConfig config, final Logger logger) {
		super(config, logger);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.warn("MySQL driver could not be registered", e);
		}
	}

	@Override
	public void startServer() {
		// implemetation is not required
	}

	@Override
	public void stopServer() {
		// implemetation is not required
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return new MySqlQueryBuilder(getDoubleConsumer());
	}

	@Override
	protected void createDb() throws SQLException {
		DriverManager
		.getConnection(
				createConnectionString(),
				createProperties()
		).createStatement()
		.executeUpdate("CREATE DATABASE IF NOT EXISTS " + config.schemaName);
	}

}
