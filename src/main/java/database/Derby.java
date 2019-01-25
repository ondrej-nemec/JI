package database;

import java.util.logging.Logger;

import common.DatabaseConfig;
import common.Os;
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
		terminal.run((a)->{}, (a)->{}, config.pathOrUrlToLocation + "/startNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() {
		terminal.run((a)->{}, (a)->{},config.pathOrUrlToLocation + "/stopNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been shutdowned");
	}

}
