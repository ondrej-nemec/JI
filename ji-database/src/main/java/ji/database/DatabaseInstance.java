package ji.database;

import java.sql.Connection;
import java.sql.SQLException;

import ji.querybuilder.QueryBuilderFactory;

public interface DatabaseInstance {
	
	void startServer();
	
	void stopServer();
	
	void createDb() throws SQLException;
	
	QueryBuilderFactory getQueryBuilderFactory(Connection connection);

}
