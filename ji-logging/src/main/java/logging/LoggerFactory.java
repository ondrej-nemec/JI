package logging;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import logging.loggers.Log4JLogger;

public class LoggerFactory {
	
	private final static String CONF_FILE = "/ji-logging.properties";
	private static final Properties PROPERTIES = new Properties();

	static {
		try (InputStreamReader isr = new InputStreamReader(LoggerFactory.class.getResourceAsStream(CONF_FILE))){
			PROPERTIES.load(isr);
		} catch (Exception e) {
			System.err.println("Cannot load " + CONF_FILE + ", default configuration used. Caused " + e.getMessage());
			// e.printStackTrace();
		}
	}
	
	private static final Map<String, Logger> cache = new HashMap<>();

	public static Logger getLogger(final String name) {
		if (cache.get(name) == null) {
			cache.put(name, new Log4JLogger(name, new LoggerConfig(PROPERTIES)));
		}
		return cache.get(name);
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
/*
	private static Logger selectLogger(String name) {
		switch (config.getLoggerType(name)) {
		case NULL: return new NullLogger();
		case CONSOLE: return new ConsoleLogger(name);
		case NATIVE: return new NativeLogger(name, config);
		case LOG4J: return new Log4JLogger(name, config);
		default:
			throw new RuntimeException("Unsupported logger " + config.getLoggerType(name));
		}
	}
*/
}
