package ji.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import ji.common.Logger;
import ji.database.wrappers.ConnectionWrapper;

public class ConnectionPool {
	
	private final static int WAIT_TIME = 30; // miliseconds
	private final static int VALID_TIMEOUT = 3; // seconds
	
	private final boolean isTemp;
	private final String connectionString;
	private final Properties prop;
	private final int maxSize;
	
	private final Logger logger;
	
	private final LinkedList<Connection> available;
	private final Map<Integer, Connection> borrowed;
	
	public ConnectionPool(String connectionString, Properties prop, int maxSize, Logger logger, boolean temp) {
		this.maxSize = maxSize;
		this.isTemp = temp;
		this.prop = prop;
		this.connectionString = connectionString;
		this.logger = logger;
		this.available = new LinkedList<>();
		this.borrowed = new HashMap<>();
	}
	
	/**
	 * BLOCKING
	 * Create connection or get from pool
	 * @return connection
	 * @throws SQLException
	 */
	public synchronized Connection getConnection() throws SQLException {
		// borrowed full - wait
		while (borrowed.size() >= maxSize) {
		//	System.err.println(String.format("No connection: A: %s, B: %s", available.size(), borrowed.size()));
			logger.debug("Connection pool is busy, waiting.");
			try {Thread.sleep(WAIT_TIME);} catch (InterruptedException e) {e.printStackTrace();}
		}
		Connection c = getAvailableConnection();
	//	System.err.println(String.format("Get connection: A: %s, B: %s", available.size(), borrowed.size()));
		c.setAutoCommit(!isTemp);
		borrowed.put(c.hashCode(), c);
		return c;
	}
	
	private Connection getAvailableConnection() throws SQLException {
		// no available - create
		if (available.size() == 0) {
			available.add(createConnection());
			return available.removeFirst();
		}
		Connection c = available.removeFirst();
		if (c.isValid(VALID_TIMEOUT)) {
			return getAvailableConnection();
		}
		return c;
	}
	
	private Connection createConnection() throws SQLException {
		Connection c = DriverManager.getConnection(connectionString, prop);
		if (Database.PROFILER == null) {
			return c;
		}
		return new ConnectionWrapper(c);
	}
	
	public void returnAllConnections() throws SQLException {		
		for (Integer key : borrowed.keySet()) {
			returnConnection(borrowed.get(key));
		}
	}
	
	public void returnConnection(Connection connection) throws SQLException {
		if (isTemp) {
			connection.rollback();
		}
		available.add(
			borrowed.remove(connection.hashCode())
		);
	//	System.err.println(String.format("Return connection: A: %s, B: %s", available.size(), borrowed.size()));
	}

}
