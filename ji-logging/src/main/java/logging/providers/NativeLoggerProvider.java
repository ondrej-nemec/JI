package logging.providers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import common.Console;
import common.OperationSystem;
import logging.LoggerConfig;
import text.BufferedWriterFactory;
import text.plaintext.PlainTextCreator;

public class NativeLoggerProvider {
	
	private final Console console;
	
	private final LoggerConfig config;
			
	public NativeLoggerProvider(final Console console, final LoggerConfig config) {
		this.config = config;
		this.console = console;
	}
	
	public Logger getLogger(String name, final Level loggeredLevel) {
		Logger logger = Logger.getLogger(name);
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);		
		
		logger.addHandler(consoleHandler(loggeredLevel));
		logger.addHandler(fileHandler(loggeredLevel, config.getPathToLogs()));
		
		return logger;
	}
	
	private Handler fileHandler(final Level loggeredLevel, final String pathToLogger) {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return loggeredLevel;
			}
			
			@Override
			public synchronized void publish(LogRecord record) {
				try(BufferedWriter bw = BufferedWriterFactory.buffer(pathToLogger, true)) {
					new PlainTextCreator(bw).write(makeMessage(record));
				} catch (IOException e) {
					//TODO what do if writing falls
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
		String result = new Date(record.getMillis()) + " | "
				+ record.getSourceClassName() + " : "
				+ record.getSourceMethodName() + " : "
				+ record.getParameters();
		result += OperationSystem.NEW_LINE;
		return result + record.getLevel() + ": " + record.getMessage();
	}
}
