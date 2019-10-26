package database;

import java.sql.Connection;
import java.sql.SQLException;

import common.Logger;
import database.support.DoubleConsumer;
import querybuilder.QueryBuilder;
import querybuilder.mysql.MySqlQueryBuilder;
import utils.Terminal;

public class Derby implements DatabaseInstance {

	private final Terminal terminal;
	
	private Logger logger;
	
	private String pathToServer;
	
	private final DoubleConsumer consumer;
	
	public Derby(final DoubleConsumer consumer, final Logger logger) {
		this.terminal = new Terminal(logger);
		this.logger = logger;
		this.consumer = consumer;
	}

	@Override
	public void startServer() { //TODO only if not on external server
	//	System.getProperties().setProperty("derby.system.home", config.pathOrUrlToLocation);
		terminal.runFile((a)->{}, (a)->{}, pathToServer + "/startNetworkServer");
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() { //TODO only if not on external server
		terminal.runFile((a)->{}, (a)->{}, pathToServer + "/stopNetworkServer");
		logger.info("Derby has been shutdowned");
	}

	@Override
	public void createDb() throws SQLException {
		consumer.accept((conn)->{/* create and close connection - create db schema */});
	}

	@Override
	public QueryBuilder getQueryBuilder(Connection connection) {
		return new MySqlQueryBuilder(connection);
	}

}
