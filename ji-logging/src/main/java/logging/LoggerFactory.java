package logging;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import logging.loggers.ConsoleLogger;
import logging.loggers.Log4JLogger;
import logging.loggers.NativeLogger;
import logging.loggers.NullLogger;

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
			cache.put(name, selectLogger(name));
		}
		return cache.get(name);
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	private static Logger selectLogger(String name) {
		
		LoggerConfig config = new LoggerConfig(PROPERTIES);		
		switch (config.getLogHander()) {
			case NULL: return new NullLogger();
			case CONSOLE: return new ConsoleLogger(name);
			case NATIVE: return new NativeLogger(name, config);
			case LOG4J: return new Log4JLogger(name, config);
			default:
			throw new RuntimeException("Unsupported handler " + config.getLogHander());
		}
	}

}
