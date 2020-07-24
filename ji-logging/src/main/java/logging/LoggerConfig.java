package logging;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class LoggerConfig {
	
	// private static final String defaultLayout = "%p %d{yyyy-MM-dd HH:mm:ss} %c{3} [%t] - %m%n"; // TODO
	
	private final Properties prop;
	
	public LoggerConfig(Properties prop) {
		this.prop = prop;
	}
	
	public LogLevel getLogLevel(String name, LoggerType type) {
		return LogLevel.valueOf(getValue(name, type, "level").toUpperCase());
	}
	
	public LogLevel getLogLevel(String name) {
		return LogLevel.valueOf(getValue(name, "level").toUpperCase());
	}
	
	public String getLoggerPath(String name, LoggerType type) {
		return getValue(name, type, "path");
	}
	
	public String getLoggerPath(String name) {
		return getValue(name, "path");
	}
	
	public String getMessagePattern(String name, LoggerType type) {
		return getValue(name, type, "pattern");
	}
	
	public List<LoggerType> getTypes(String name) {
		String logger = getValue(name, "types");
		List<LoggerType> types = new LinkedList<>();
		String[] ty = logger.split(",");
		for (String type : ty) {
			types.add(LoggerType.valueOf(type.toUpperCase()));
		}
		return types;
	}
	

	private String getValue(String name, String property) {
		String root = prop.getProperty(property) == null ? LogLevel.INFO.toString() : prop.getProperty(property);
		String logger = prop.getProperty(name + "." + property) == null ? root : prop.getProperty(name + "." + property);
		return logger;
	}
	
	private String getValue(String name, LoggerType type, String property) {
		String logger = getValue(name, property);
		String appender = prop.getProperty(name + "." + property + "." + type.toString().toLowerCase()) == null 
				? logger : prop.getProperty(name + "." + property + "." + type.toString().toLowerCase());
		return appender;
	}
	
	
/*	
	public type gettype(String name) {
		type defType = prop.getProperty("log-type") == null ? type.LOG4J : type.valueOf(prop.getProperty("log-type"));
		String type = prop.getProperty("log-type." + name);
		return type == null ? defType : type.valueOf(type);
	}

	public List<LogHandler> getLogHandlers(String name) {
		String defHandlers = prop.getProperty("path") == null ? "logs/" : prop.getProperty("path");
		String path = prop.getProperty("path." + name);
		return  path == null ? defPath : path;
	}
	
	public String getLogPath(String name) {
		String defPath = prop.getProperty("path") == null ? "logs/" : prop.getProperty("path");
		String path = prop.getProperty("path." + name);
		return  path == null ? defPath : path;
	}
	
	public LogLevel getMinLogLevel(String name) {
		LogLevel defLevel = prop.getProperty("min-level") == null ? LogLevel.ALL : LogLevel.valueOf(prop.getProperty("min-level"));
		String level = prop.getProperty("min-level." + name);
		return  level == null ? defLevel : LogLevel.valueOf(level);
	}
	
	public String getLayout(String name, LogHandler logType) {
		String defLayout = prop.getProperty("layout") == null ? defaultLayout : prop.getProperty("layout");
		String layout = prop.getProperty("layout." + name);
		return  layout == null ? defLayout : layout;
	}
	*/
}
