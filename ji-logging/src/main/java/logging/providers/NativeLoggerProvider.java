package logging.providers;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import common.functions.Console;
import core.text.Text;
import core.text.basic.WriteText;
import logging.LogLevel;
import logging.LoggerConfig;

public class NativeLoggerProvider {
	
	private final Console console;
	
	private final LoggerConfig config;
			
	public NativeLoggerProvider(final Console console, final LoggerConfig config) {
		this.config = config;
		this.console = console;
	}
	
	public Logger getLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(switchLevel(config.getLogLevel(name)));
		logger.setUseParentHandlers(false);		
		
		config.getTypes(name).forEach((type)->{
			switch (type) {
			case CONSOLE:
				logger.addHandler(consoleHandler(switchLevel(config.getLogLevel(name, type))));
				break;
			case FILE:
				logger.addHandler(fileHandler(switchLevel(config.getLogLevel(name, type)), config.getLoggerPath(name, type)));
				break;
			default:
				break;
			}
		});
		
		return logger;
	}
	
	private Level switchLevel(LogLevel minLogLevel) {
		switch (minLogLevel) {
    		case NO_LOG: return Level.OFF;
    		case FATAL: return Level.SEVERE;
    		case ERROR: return Level.WARNING;
    		case WARNING: return Level.INFO;
    		case INFO: return Level.CONFIG;
    		case DEBUG: return Level.FINE;
    		case ALL:
    		default: return Level.ALL;
		}
	}
	
	private Handler fileHandler(final Level loggeredLevel, final String pathToLogger) {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return loggeredLevel;
			}
			
			@Override
			public synchronized void publish(LogRecord record) {
				try {
					Text.write((bw)->{
						WriteText.write(bw, makeMessage(record));
					}, pathToLogger, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void flush() { /* implement me if you wish	*/	}
			
			@Override
			public void close() throws SecurityException { /* implement me if you wish	*/ }
		};
	}
	
	private Handler consoleHandler (final Level loggeredLevel) {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return loggeredLevel;
			}
			
			@Override
			public synchronized void publish(LogRecord record) {
				if (record.getLevel().intValue() >= Level.CONFIG.intValue())
					console.err(makeMessage(record));
				else
					console.out(makeMessage(record));
			}
			
			@Override
			public void flush() { /* implement me if you wish	*/	}
			
			@Override
			public void close() throws SecurityException { /* implement me if you wish	*/ }
		};
	}
	
	private String makeMessage(final LogRecord record) {
		// "%p %d{yyyy-MM-dd HH:mm:ss} %c{3} [%t] (%F:%L) - %m%n";
		StringBuilder message = new StringBuilder();
		message.append(record.getLevel());
		message.append(" ");
		message.append(new Date(record.getMillis()));
		message.append(" ");
		message.append(String.format("[%s]", record.getThreadID()));
		message.append(" ");
		message.append(String.format("%s:%s", record.getSourceClassName(), record.getSourceMethodName()));
		message.append(" ");
		message.append(record.getMessage());
		message.append(" ");
		return message.toString();
		/*
		String result = new Date(record.getMillis()) + " | "
				+ record.getSourceClassName() + " : "
				+ record.getSourceMethodName() + " : "
				+ record.getParameters();
		result += OperationSystem.NEW_LINE;
		return result + record.getLevel() + ": " + record.getMessage();
		*/
	}
}
