package logging;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import common.functions.InputStreamLoader;
import logging.loggers.ConsoleLogger;
import logging.loggers.Log4JLogger;
import logging.loggers.NativeLogger;
import logging.loggers.NullLogger;

public class LoggerFactory {
	
	private final static String CONF_FILE = "ji-logging.properties";
	private static final Properties PROPERTIES = new Properties();
	private static final Map<String, Logger> CACHE = new HashMap<>();

	public static Logger getLogger(final String name) {
		if (CACHE.get(name) == null) {
			CACHE.put(name, selectLogger(name));
		}
		return CACHE.get(name);
	}

	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	public static void setConfigFile(String file) {
		if (PROPERTIES.isEmpty()) {
			loadProperties(file);
		} else {
			throw new RuntimeException("Properties was setted before: " + CONF_FILE);
		}
	}
	
	private static Logger selectLogger(String name) {
		if (PROPERTIES.isEmpty()) {
			loadProperties(CONF_FILE);
		}
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
	
	private static void loadProperties(String file) {
		try (InputStreamReader isr = new InputStreamReader(InputStreamLoader.createInputStream(LoggerFactory.class, file))){
			PROPERTIES.load(isr);
		} catch (Exception e) {
			System.err.println("Cannot load " + file + ", default configuration used. Caused " + e.getMessage());
			// e.printStackTrace();
		}
	}

}
