package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import common.Logger;
import querybuilder.QueryBuilder;
import querybuilder.derby.DerbyQueryBuilder;
import utils.Terminal;

public class Derby implements DatabaseInstance {

	private final Terminal terminal;
	
	private final Logger logger;
	
	private final String pathToServer;
	
	private final String connectionString;
	
	private final Properties property;
	
	private final boolean runOnExternal;
	
	public Derby(boolean runOnExternal, final String pathToServer, String connectionString, Properties property, final Logger logger) {
		this.terminal = new Terminal(logger);
		this.logger = logger;
		this.connectionString = connectionString;
		this.property = property;
		this.pathToServer = pathToServer;
		this.runOnExternal = runOnExternal;
	}

	@Override
	public void startServer() {
		if (runOnExternal) {
			logger.info("Signal Start DB server not sended because server is not under app manage");
		} else {
		//	System.getProperties().setProperty("derby.system.home", config.pathOrUrlToLocation);
			terminal.runFile(pathToServer + "/startNetworkServer");
			logger.info("Derby has been started");
		}
	}

	@Override
	public void stopServer() {
		if (runOnExternal) {
			logger.info("Signal Stop DB server not sended because server is not under app manage");
		} else {
			terminal.runFile(pathToServer + "/stopNetworkServer");
			logger.info("Derby has been shutdowned");			
		}
	}

	@Override
	public void createDb() throws SQLException {
		try (Connection con = DriverManager.getConnection(connectionString, property)) {
			/* create and close connection - create db schema */
		}
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		return new DerbyQueryBuilder(connection);
	}

}
