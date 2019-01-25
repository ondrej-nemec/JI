package database;

import java.util.logging.Logger;

import common.DatabaseConfig;

public class MySQL extends Database {

	public MySQL(final DatabaseConfig config, final Logger logger) {
		super(config, logger);
	}
	
	@Override
	public void startServer() {
		// implemetation is not required
	}

	@Override
	public void stopServer() {
		// implemetation is not required
	}

}
