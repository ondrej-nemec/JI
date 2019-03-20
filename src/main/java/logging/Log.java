package logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import common.Console;
import common.Os;
import text.BufferedWriterFactory;
import text.plaintext.PlainTextCreator;

public class Log {
	
	public static final int CONSOLE = 0;
	
	public static final int FILE = 1;
	
	private final Console console;
	
	private final String logDir;
		
	public Log(final Console console, final String logDir) {
		this.logDir = logDir;
		this.console = console;
	}
	
	public Logger addAllHandlers(final Logger logger) {
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);		
		
		logger.addHandler(consoleHandler());
		logger.addHandler(fileHandler(logger.getName()));
		
		return logger;
	}
	
	public Logger setHandler(final Logger logger, int... handlers) {
		logger.setLevel(Level.ALL);
		logger.setUseParentHandlers(false);
		
		for (int handler : handlers) {
			switch(handler) {
			case CONSOLE: logger.addHandler(consoleHandler()); break;
			case FILE: logger.addHandler(fileHandler(logger.getName())); break;
			}
		}
		
		return logger;
	}
	
	public Handler fileHandler(final String loggerName) {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return Level.ALL;
			}
			
			@Override
			public synchronized void publish(LogRecord record) {
				String fileName = loggerName;
				if (fileName == null)
					fileName = "_default";
				fileName = logDir + fileName + ".log";
				
				try(BufferedWriter bw = BufferedWriterFactory.buffer(fileName, true)) {
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
	
	public Handler consoleHandler () {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return Level.ALL;
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
		result += Os.getNewLine();		
		return result + record.getLevel() + ": " + record.getMessage();
	}
}
