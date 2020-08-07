package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import common.Logger;
import querybuilder.QueryBuilder;

public class SqlServer implements DatabaseInstance {
	
	private final Logger logger;
	
	private final String connectionString;
	
	private final Properties property;
	
	private final String name;

	public SqlServer(String connectionString, Properties property, String name, final Logger logger) {
		this.logger = logger;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		// TODO Auto-generated method stub
		return null;
	}

}
