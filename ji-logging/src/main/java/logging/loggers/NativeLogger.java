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
		
		this.logger = log.getLogger(name, Level.ALL);
	}

	@Override
	public void debug(Object message) {
		logger.fine(message.toString());
	}

	@Override
	public void debug(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(Object message) {
		logger.info(message.toString());
	}

	@Override
	public void info(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(Object message) {
		logger.warning(message.toString());
	}

	@Override
	public void warn(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Object message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void error(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatal(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatal(Object message, Throwable t) {
		// TODO Auto-generated method stub
		
	}

}
