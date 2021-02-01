package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import common.Logger;
import common.exceptions.NotImplementedYet;
import querybuilder.QueryBuilder;
import querybuilder.postgresql.PostgreSqlQueryBuilder;

public class PosgreSql implements DatabaseInstance {
	
	private final String connectionString;
	
	private final Properties property;
	
	private final String name;

	private final boolean runOnExternal;
	
	private final Logger logger;
	
	public PosgreSql(boolean runOnExternal, String connectionString, Properties property, String name, Logger logger) {
		this.connectionString = connectionString;
		this.property = property;
		this.runOnExternal = runOnExternal;
		this.logger = logger;
		this.name = name;
		try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
        	this.logger.warn("MySQL driver could not be registered", e);
        }
	}

	@Override
	public void startServer() {
		if (runOnExternal) {
			logger.info("Signal Start DB server not sended because server is not under app manage");
		} else {
			throw new NotImplementedYet(); // TODO
		}
	}

	@Override
	public void stopServer() {
		if (runOnExternal) {
			logger.info("Signal Stop DB server not sended because server is not under app manage");
		} else {
			throw new NotImplementedYet(); // TODO
		}
	}

	@Override
	public void createDb() throws SQLException {
		try (Connection con = DriverManager.getConnection(connectionString, property)) {
			PreparedStatement stmt = con.prepareStatement("SELECT FROM pg_database WHERE datname = ?");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				con.createStatement().executeUpdate(String.format("CREATE DATABASE %s", name));
			}
		}
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		return new PostgreSqlQueryBuilder(connection);
	}

}
