package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import common.Env;
import common.Os;
import common.Terminal;

public class Derby implements Database {

	private final Terminal terminal;
	
	private final Env env;
	
	private Connection con = null;
	
	private final Logger logger;
	
	private String pathToDerbyBin = ""; //TODO from env? or fixed?
	
	public Derby(Terminal terminal, Env env, Logger logger) {
		this.terminal = terminal;
		this.env = env;
		this.logger = logger;
	}
	
	@Override
	public void startServer() {
		terminal.run((a)->{}, (a)->{}, pathToDerbyBin + "startNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been started");
	}

	@Override
	public void stopServer() {
		terminal.run((a)->{}, (a)->{}, pathToDerbyBin + "stopNetworkServer" + Os.getCliExtention());
		logger.info("Derby has been shutdowned");
	}

	@Override
	public Connection getConnnection() throws SQLException {
		if (con == null) {
			Properties props = new Properties();
			props.setProperty("create", "true");
			props.setProperty("user", env.databaseLogin);
			props.setProperty("password", env.databasePassword);
			
			con = DriverManager.getConnection("jdbc:derby:" + env.pathToAppWorkspace + env.databaseName, props);
		}
		return con;
	}

	@Override
	public void stopConnection() throws SQLException {
		if (con != null)
			con.close();
	}

}
