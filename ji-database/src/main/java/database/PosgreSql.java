package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import common.exceptions.NotImplementedYet;
import querybuilder.QueryBuilder;

public class PosgreSql implements DatabaseInstance {
	
	private final String connectionString;
	
	private final Properties property;
	
	private final String name;
	
	public PosgreSql(String connectionString, Properties property, String name) {
		this.connectionString = connectionString;
		this.property = property;
		this.name = name;
	}

	@Override
	public void startServer() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub
	}

	@Override
	public void createDb() throws SQLException {
		DriverManager
		.getConnection(connectionString, property)
		.createStatement()
		.executeUpdate(String.format(
				"IF EXISTS (SELECT FROM pg_database WHERE datname = '%s') THEN"
				+ "   ELSE CREATE DATABASE %s"
				+ "END IF ", name, name));
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		throw new NotImplementedYet();
	}

}
