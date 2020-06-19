package database;

import java.sql.Connection;
import java.sql.SQLException;

import common.Logger;
import database.support.DoubleConsumer;
import querybuilder.QueryBuilder;
import querybuilder.derby.DerbyQueryBuilder;
import utils.Terminal;

public class Derby implements DatabaseInstance {

	private final Terminal terminal;
	
	private final Logger logger;
	
	private final String pathToServer;
	
	private final DoubleConsumer consumer;
	
	public Derby(final String pathToServer, final DoubleConsumer consumer, final Logger logger) {
		this.terminal = new Terminal(logger);
		this.logger = logger;
		this.consumer = consumer;
		this.pathToServer = pathToServer;
	}

	@Override
	public void startServer() { //TODO only if not on external server
	//	System.getProperties().setProperty("derby.system.home", config.pathOrUrlToLocation);
		terminal.runFile(pathToServer + "/startNetworkServer");
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() { //TODO only if not on external server
		terminal.runFile(pathToServer + "/stopNetworkServer");
		logger.info("Derby has been shutdowned");
	}

	@Override
	public void createDb() throws SQLException {
		consumer.accept((conn)->{/* create and close connection - create db schema */});
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		return new DerbyQueryBuilder(connection);
	}

}
