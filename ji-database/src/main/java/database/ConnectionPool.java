package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.Logger;

public class ConnectionPool {
	
	private final static int WAIT_TIME = 30;
	
	private final int maxSize;
	
	private final Map<Integer, Connection> pool;
	
	private final boolean isTemp;
	
	private final String connectionString;
	
	private final Properties prop;
	
	private final Logger logger;
	
	public ConnectionPool(String connectionString, Properties prop, int maxSize, Logger logger, boolean temp) {
		this.maxSize = maxSize;
		this.isTemp = temp;
		this.prop = prop;
		this.connectionString = connectionString;
		this.pool = new HashMap<>();
		this.logger = logger;
	}
	
	public Connection getConnection() throws SQLException {		
		while (pool.size() >= maxSize) {
			logger.debug("Connection pool is busy, waiting.");
			try {Thread.sleep(WAIT_TIME);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		Connection c = DriverManager.getConnection(connectionString, prop);
		c.setAutoCommit(!isTemp);
		pool.put(c.hashCode(), c);
		return c;
	}
	
	public void returnAllConnections() throws SQLException {		
		for (Integer key : pool.keySet()) {
			returnConnection(pool.get(key));
		}
	}
	
	public void returnConnection(Connection connection) throws SQLException {
		if (isTemp) {
			connection.rollback();
		}
		connection.close();
		pool.remove(connection.hashCode());
	}

}
