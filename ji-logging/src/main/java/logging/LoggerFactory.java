package logging;

import common.Logger;
import logging.loggers.ConsoleLogger;
import logging.loggers.Log4JLogger;
import logging.loggers.NullLogger;
import utils.env.LoggerConfig;

public class LoggerFactory {
	
	private final LoggerConfig config;
	
	public LoggerFactory(final LoggerConfig config) {
		this.config = config;
	}
	
	public Logger getLogger(final String name) {
		//TODO switch by data from env
		switch(config.getAppMode()) {
			case PROD:
			case DEV: return Log4JLogger.createLog4JLogger(name, config);
			case TEST: return new ConsoleLogger(name);
			default: return new NullLogger();
		}
	}
	
	public Logger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
}
