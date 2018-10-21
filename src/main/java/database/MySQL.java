package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import common.Env;

public class MySQL implements Database {

	private Connection con = null;
	
	private final Env env;
	
	public MySQL(Env env) {
		this.env = env;
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
	public Connection getConnnection() throws SQLException {
		if (con == null) {
			con = DriverManager.getConnection(env.databaseUrlConnection);
		}
		return con;
	}

	@Override
	public void stopConnection() throws SQLException {
		if (con != null)
			con.close();
	}

}
