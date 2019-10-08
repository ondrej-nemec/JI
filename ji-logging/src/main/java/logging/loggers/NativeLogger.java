package logging.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import common.Console;
import logging.LogLevel;
import logging.LoggerConfig;
import logging.providers.NativeLoggerProvider;

public class NativeLogger implements common.Logger{
	
	private final Logger logger;
	
	private final ConsoleLogger console;
	
	public NativeLogger(final String name, final LoggerConfig config) {
		NativeLoggerProvider log = new NativeLoggerProvider(new Console(), config);
		Level level = switchLevel(config.getMinLogLevel());
		this.logger = log.getLogger(name, level);
		this.console = new ConsoleLogger(name);
	}

	@Override
	public void debug(Object message) {
		console.debug(message);
		logger.fine(message.toString());
	}

	@Override
	public void debug(Object message, Throwable t) {
		console.debug(message, t);
		logger.log(Level.FINE, message.toString(), t);
	}

	@Override
	public void info(Object message) {
		console.info(message);
		logger.config(message.toString());
	}

	@Override
	public void info(Object message, Throwable t) {
		console.info(message, t);
		logger.log(Level.CONFIG, message.toString(), t);
	}

	@Override
	public void warn(Object message) {
		console.warn(message);
		logger.info(message.toString());
	}

	@Override
	public void warn(Object message, Throwable t) {
		console.warn(message, t);
		logger.log(Level.INFO, message.toString(), t);
	}

	@Override
	public void error(Object message) {
		console.error(message);
		logger.warning(message.toString());
	}

	@Override
	public void error(Object message, Throwable t) {
		console.error(message, t);
		logger.log(Level.WARNING, message.toString(), t);
	}

	@Override
	public void fatal(Object message) {
		console.fatal(message);
		logger.severe(message.toString());
	}

	@Override
	public void fatal(Object message, Throwable t) {
		console.fatal(message, t);
		logger.log(Level.SEVERE, message.toString(), t);
	}
	
	/*********************************/

	private Level switchLevel(LogLevel minLogLevel) {
		switch (minLogLevel) {
    		case NO_LOG: return Level.OFF;
    		case FATAL: return Level.SEVERE;
    		case ERROR: return Level.WARNING;
    		case WARNING: return Level.INFO;
    		case INFO: return Level.CONFIG;
    		case DEBUG: return Level.FINE;
    		default: return Level.ALL;
		}
	}

}
