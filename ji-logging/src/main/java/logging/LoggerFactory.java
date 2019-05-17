package logging;

import common.Logger;
import logging.loggers.ConsoleLogger;
import logging.loggers.Log4JLogger;
import logging.loggers.NativeLogger;
import logging.loggers.NullLogger;
import utils.env.LoggerConfig;

public class LoggerFactory {
	
	private final LoggerConfig config;
	
	public LoggerFactory(final LoggerConfig config) {
		this.config = config;
	}
	
	public Logger getLogger(final String name) {		
		switch (config.getLoggerType().toLowerCase()) {
		case "null": return new NullLogger();
		case "console": return new ConsoleLogger(name);
		case "native": return new NativeLogger(name, config);
		case "log4j": return new Log4JLogger(name, config);
		default:
			throw new RuntimeException(
				"Unsupported logger " + config.getLoggerType()
				+ ". Supported (ci): 'null', 'console', 'native', 'log4j'"
			);
		}
	}
	
	public Logger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
}
