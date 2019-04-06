package logging;

import common.Env;
import logging.loggers.Log4JLogger;
import logging.loggers.NullLogger;

public class Logger {
	
	private static Env env = null;
	
	public static ILogger getLogger(final String name) {
		if (env == null) {
			Log4JLogger.clearConfiguration();
			return new NullLogger();
		}
		return new Log4JLogger(name, env.mode, env.getProperty("log.pathToLogs"));
		//TODO switch by data from env
		/*switch(env.mode) {
			case PROD:
			case DEV: return new ConsoleLogger(name);
			case TEST:
			default: return new NullLogger();
		}*/
	}
	
	public static ILogger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
	public static void setEnvIfNotSetted(Env env) {
		if (env == null)
			Logger.env = env;
	}

}
