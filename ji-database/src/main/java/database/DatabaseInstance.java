package database;

import java.sql.SQLException;

import database.support.DoubleConsumer;
import querybuilder.QueryBuilder;

public interface DatabaseInstance {
	
	void startServer();
	
	void stopServer();
	
	void createDb() throws SQLException;
	
	QueryBuilder getQueryBuilder(DoubleConsumer consumer);

}
