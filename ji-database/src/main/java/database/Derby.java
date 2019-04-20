package database;

import common.ILogger;
import utils.Terminal;
import utils.env.DatabaseConfig;

public class Derby extends Database {

	private final Terminal terminal;
	
	public Derby(final DatabaseConfig config, final ILogger logger) {
		super(config, logger);
		this.terminal = new Terminal(logger);
	}

	@Override
	public void startServer() {
		System.getProperties().setProperty("derby.system.home", config.pathOrUrlToLocation);
		terminal.runFile((a)->{}, (a)->{}, config.pathOrUrlToLocation + "/startNetworkServer");
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() {
		terminal.runFile((a)->{}, (a)->{},config.pathOrUrlToLocation + "/stopNetworkServer");
		logger.info("Derby has been shutdowned");
	}

}
