package logging;

import common.ILogger;
import logging.loggers.ConsoleLogger;
import utils.Env;

public class Logger {
	
	private final Env env;
	
	public Logger(final Env env) { // TODO config
		this.env = env;
	}
	
	public ILogger getLogger(final String name) {
		//TODO 
		env.createDbConfig();
		
		return new ConsoleLogger(name);
		/*if (env == null) {
			Log4JLogger.clearConfiguration();
			return new NullLogger();
		}
		return new Log4JLogger(name, env.mode, env.getProperty("log.pathToLogs"));*/
		//switch by data from env
		/*switch(env.mode) {
			case PROD:
			case DEV: return new ConsoleLogger(name);
			case TEST:
			default: return new NullLogger();
		}*/
	}
	
	public ILogger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
}
