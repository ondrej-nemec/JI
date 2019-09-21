package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import common.Logger;
import database.mysql.MySqlQueryBuilder;
import database.support.DoubleConsumer;
import querybuilder.QueryBuilder;

public class MySql implements DatabaseInstance {
	
	private final Logger logger;
	
	private final String connectionString;
	
	private final Properties property;
	
	private final String name;

	public MySql(String connectionString, Properties property, String name, final Logger logger) {
		this.logger = logger;
		this.connectionString = connectionString;
		this.property = property;
		this.name = name;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			this.logger.warn("MySQL driver could not be registered", e);
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
	public void createDb() throws SQLException {
		DriverManager
    		.getConnection(connectionString, property)
    		.createStatement()
    		.executeUpdate("CREATE DATABASE IF NOT EXISTS " + name);
	}

	@Override
	public QueryBuilder getQueryBuilder(DoubleConsumer consumer) {
		return new MySqlQueryBuilder(consumer);
	}

}
