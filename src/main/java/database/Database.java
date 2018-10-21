package database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {

	void startServer();
	
	void stopServer();
	
	Connection getConnnection() throws SQLException;
	
	void stopConnection() throws SQLException;
}
