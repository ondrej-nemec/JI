package database;

import java.util.logging.Logger;

import common.Env;

public class MySQL extends Database {

	public MySQL(Env env, Logger logger) {
		super(env, logger);
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
