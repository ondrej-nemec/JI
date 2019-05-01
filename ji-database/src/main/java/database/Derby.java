package database;

import java.sql.SQLException;

import common.ILogger;
import querybuilder.DeleteQueryBuilder;
import querybuilder.InsertQueryBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.UpdateQueryBuilder;
import utils.Terminal;
import utils.env.DatabaseConfig;

public class Derby extends Database {

	private final Terminal terminal;
	
	public Derby(final DatabaseConfig config, final ILogger logger) {
		super(config, logger);
		this.terminal = new Terminal(logger);
	}

	@Override
	public void startServer() { //TODO only if not on external server
	//	System.getProperties().setProperty("derby.system.home", config.pathOrUrlToLocation);
		terminal.runFile((a)->{}, (a)->{}, config.pathOrUrlToLocation + "/startNetworkServer");
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() { //TODO only if not on external server
		terminal.runFile((a)->{}, (a)->{},config.pathOrUrlToLocation + "/stopNetworkServer");
		logger.info("Derby has been shutdowned");
	}

	@Override
	public SelectQueryBuilder getSelectBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateQueryBuilder getUpdateBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteQueryBuilder getDeletetBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsertQueryBuilder getInsertBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void createDb() throws SQLException {
		applyQuery((conn)->{/* create and close connection - create db schema */});
	}

}
