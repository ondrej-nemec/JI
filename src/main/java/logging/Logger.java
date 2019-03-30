package logging;

import common.Env;

public class Logger {
	
	private static Env env = null;
	
	public static ILogger getLogger(final String name) {
		System.out.println("get logger " + name);
		if (env == null)
			return new NullLogger();
		//TODO switch by data from env
		switch(env.mode) {
			case PROD:
			case DEV: return new ConsoleLogger(name);
			case TEST:
			default: return new NullLogger();
		}
	}
	
	public static ILogger getLogger(@SuppressWarnings("rawtypes") final Class clazz) {
		return getLogger(clazz.getName());
	}
	
	public static void setEnvIfNotSetted(Env env) {
		System.out.println("env setted");
		if (env == null)
			Logger.env = env;
	}
	
	public Env getEnv() {
		return env;
	}

}
