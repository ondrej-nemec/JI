package database;

import common.DatabaseConfig;

public class MySQL extends Database {

	public MySQL(final DatabaseConfig config) {
		super(config);
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
