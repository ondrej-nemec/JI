package logging.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import common.Console;
import logging.providers.NativeLoggerProvider;
import utils.env.LoggerConfig;

public class NativeLogger implements common.Logger{
	
	private final Logger logger;
	
	public NativeLogger(final String name, final LoggerConfig config) {
		NativeLoggerProvider log = new NativeLoggerProvider(new Console(), config);
		Level level;
		switch (config.getAppMode()) {
		case TEST:
			level = Level.OFF;
			break;
		case DEV:
			level = Level.ALL;
			break;
		case PROD:
			level = Level.CONFIG;
			break;
		default: throw new RuntimeException("Unsupported app mode " + config.getAppMode());
	}
		this.logger = log.getLogger(name, level);
	}

	@Override
	public void debug(Object message) {
		logger.fine(message.toString());
	}

	@Override
	public void debug(Object message, Throwable t) {
		logger.log(Level.FINE, message.toString(), t);
	}

	@Override
	public void info(Object message) {
		logger.config(message.toString());
	}

	@Override
	public void info(Object message, Throwable t) {
		logger.log(Level.CONFIG, message.toString(), t);
	}

	@Override
	public void warn(Object message) {
		logger.info(message.toString());
	}

	@Override
	public void warn(Object message, Throwable t) {
		logger.log(Level.INFO, message.toString(), t);
	}

	@Override
	public void error(Object message) {
		logger.warning(message.toString());
	}

	@Override
	public void error(Object message, Throwable t) {
		logger.log(Level.WARNING, message.toString(), t);
	}

	@Override
	public void fatal(Object message) {
		logger.severe(message.toString());
	}

	@Override
	public void fatal(Object message, Throwable t) {
		logger.log(Level.SEVERE, message.toString(), t);
	}

}
