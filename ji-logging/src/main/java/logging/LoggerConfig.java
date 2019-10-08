package logging;

public class LoggerConfig {
	
	private final String pathToLogs;
	
	private final LoggerType type;
	
	// private final LoggerMedium medias;

	private final LogLevel minLogLevel;
	
	public LoggerConfig(String pathToLogs, LoggerType type, LogLevel minLogLevel) {
		super();
		this.pathToLogs = pathToLogs;
		this.type = type;
		this.minLogLevel = minLogLevel;
	}

	public String getPathToLogs() {
		return pathToLogs;
	}

	public LoggerType getType() {
		return type;
	}

	public LogLevel getMinLogLevel() {
		return minLogLevel;
	}

}
