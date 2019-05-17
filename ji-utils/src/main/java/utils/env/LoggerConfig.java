package utils.env;

import utils.enums.AppMode;

public class LoggerConfig {
	
	private final AppMode appMode;
	
	private final String pathToLogs;
	
	private final String loggerType;

	public LoggerConfig(AppMode appMode, String loggerType, String pathToLogs) {
		this.appMode = appMode;
		this.pathToLogs = pathToLogs;
		this.loggerType = loggerType;
	}

	public AppMode getAppMode() {
		return appMode;
	}

	public String getPathToLogs() {
		return pathToLogs;
	}

	public String getLoggerType() {
		return loggerType;
	};	

}
