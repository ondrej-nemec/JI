package database;

import java.util.logging.Logger;

import common.DatabaseConfig;
import common.Terminal;

public class Derby extends Database {

	private final Terminal terminal;
	
	private final Logger logger;
	
	public Derby(final DatabaseConfig config, final Logger logger, final Terminal terminal) {
		super(config, logger);
		this.terminal = terminal;
		this.logger = logger;
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
