package logging;

import common.Env;

public class Logger {
	
	private static Env env = null;
	
	public static ILogger getLogger(final Object name) {
		if (env == null)
			return new NullLogger();
		switch(env.mode) {
			case TEST:
			case DEV:
			case PROD:
			default: return new NullLogger();
		}
	}
	
	public static ILogger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
	public static void setMode(Env env) {
		if (env == null)
			Logger.env = env;
	}

}
