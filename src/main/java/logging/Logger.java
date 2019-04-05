package logging;

import common.Env;

public class Logger {
	
	private static Env env = null;
	
	public static ILogger getLogger(final String name) {
		if (env == null)
			return new ConsoleLogger(name);
			//return new NullLogger();
		//TODO switch by data from env
		switch(env.mode) {
			case PROD:
			case BETA:
			case DEV: return new ConsoleLogger(name);
			case TEST:
			default: return new NullLogger();
		}
	}
	
	public static ILogger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
	public static void setEnvIfNotSetted(Env env) {
		if (env == null)
			Logger.env = env;
	}

}
