package logging;

import java.util.HashMap;
import java.util.Map;

import common.Logger;
import logging.loggers.Log4JLogger;
import logging.loggers.NativeLogger;
import logging.loggers.NullLogger;

public class LoggerFactory {

	private final LoggerConfig config;

	private final Map<String, Logger> loggers;

	public LoggerFactory(final LoggerConfig config) {
		this.config = config;
		this.loggers = new HashMap<>();
	}

	public Logger getLogger(final String name) {
		Logger l = loggers.get(name);
		if (l == null) {
			l = selectLogger(name);
			loggers.put(name, l);
		}
		return l;
	}

	public Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	private Logger selectLogger(String name) {
		switch (config.getType()) {
		case NULL: return new NullLogger();
		// case "console": return new ConsoleLogger(name);
		case NATIVE: return new NativeLogger(name, config);
		case LOG4J: return new Log4JLogger(name, config);
		default:
			throw new RuntimeException("Unsupported logger " + config.getType());
		}
	}
	
}
