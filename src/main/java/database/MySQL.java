package database;

public class MySQL extends Database {

	public MySQL(final DatabaseConfig config) {
		super(config);
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
