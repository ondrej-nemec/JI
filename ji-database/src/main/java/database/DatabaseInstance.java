package database;

import java.sql.Connection;
import java.sql.SQLException;

import querybuilder.QueryBuilder;

public interface DatabaseInstance {
	
	void startServer();
	
	void stopServer();
	
	void createDb() throws SQLException;
	
	QueryBuilder getQueryBuilder(Connection connection);

}
