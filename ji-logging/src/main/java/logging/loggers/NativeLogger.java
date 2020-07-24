package logging.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import common.Console;
import logging.LoggerConfig;
import logging.providers.NativeLoggerProvider;

public class NativeLogger extends Logger implements common.Logger{
	
	private final Logger logger;
	
	private final ConsoleLogger console;
	
	public NativeLogger(final String name, final LoggerConfig config) {
		super(name, "");
		NativeLoggerProvider log = new NativeLoggerProvider(new Console(), config);
		this.logger = log.getLogger(name);
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
	
}
