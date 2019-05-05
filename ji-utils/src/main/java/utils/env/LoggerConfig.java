package utils.env;

import utils.enums.AppMode;

public class LoggerConfig {
	
	private final AppMode appMode;
	
	private final String pathToLogs;

	public LoggerConfig(AppMode appMode, String pathToLogs) {
		this.appMode = appMode;
		this.pathToLogs = pathToLogs;
	}

	public AppMode getAppMode() {
		return appMode;
	}

	public String getPathToLogs() {
		return pathToLogs;
	};
	
	

}
