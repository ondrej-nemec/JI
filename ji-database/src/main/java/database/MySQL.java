package database;

import common.ILogger;
import utils.env.DatabaseConfig;

public class MySQL extends Database {

	public MySQL(final DatabaseConfig config, final ILogger logger) {
		super(config, logger);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.warn("MySQL driver could not be registered", e);
		}
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
