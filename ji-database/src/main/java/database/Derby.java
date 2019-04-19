package database;
/*
import common.Terminal;
import utils.env.DatabaseConfig;
*/
public class Derby extends Database {

	@Override
	public void startServer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub
		
	}
/*
	private final Terminal terminal;
	
	public Derby(final DatabaseConfig config) {
		super(config);
		this.terminal = new Terminal();
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
*/
}
