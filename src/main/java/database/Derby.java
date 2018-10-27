package database;

import java.util.logging.Logger;

import common.Env;
import common.Os;
import common.Terminal;

public class Derby extends Database {

	private final Terminal terminal;
	
	private final Logger logger;
	
	private String databasesStorage;
	
	public Derby(Env env, Logger logger, Terminal terminal, String databasesStorage) {
		super(env, logger);
		this.terminal = terminal;
		this.logger = logger;
		this.databasesStorage = databasesStorage;
	}

	@Override
	public void startServer() {
		if (databasesStorage != "")
			System.getProperties().setProperty("derby.system.home", databasesStorage);
		terminal.run((a)->{}, (a)->{}, env.databaseLocation + "/startNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() {
		terminal.run((a)->{}, (a)->{}, env.databaseLocation + "/stopNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been shutdowned");
	}

}
