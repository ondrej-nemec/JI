package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import querybuilder.QueryBuilder;
import querybuilder.mysql.MySqlQueryBuilder;

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
		return new MySqlQueryBuilder(connection); // TODO fix
	}

}
