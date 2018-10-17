package common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import text.plaintext.PlainTextCreator;

public class Log {
	
	private final Console console;
	
	private final String logDir;
	
	private final PlainTextCreator creator;
	
	public Log(Console console, String logDir) {
		this(console, logDir, new PlainTextCreator());
	}
	
	public Log(Console console, String logDir, PlainTextCreator creator) {
		this.logDir = logDir + "/";
		this.creator = creator;
		this.console = console;
	}
	
	public void setLogger(Logger logger) {
		logger.setLevel(Level.ALL);
				
		logger.setUseParentHandlers(false);
		logger.addHandler(consoleHandler());
		logger.addHandler(fileHandler(logger.getName()));
	}
	
	public Handler fileHandler(String loggerName) {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return Level.ALL;
			}
			
			@Override
			public void publish(LogRecord record) {
				String fileName = loggerName;
				if (fileName == null)
					fileName = "_default";
				fileName = logDir + fileName + ".log";
				
				try(BufferedWriter bw = creator.buffer(fileName, true)) {
					creator.write(bw, makeMessage(record));
				} catch (IOException e) {
					//TODO what do if writing falls
					e.printStackTrace();
				}
			}
			
			@Override
			public void flush() {
				// implement me if you wish				
			}
			
			@Override
			public void close() throws SecurityException {
				// implement me if you wish			
			}
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
			public void flush() {
				// implement me if you wish
			}
			
			@Override
			public void close() throws SecurityException {
				// implement me if you wish
			}
		};
	}
	
	private String makeDetailedMessage(LogRecord record) {
		Date d = new Date(record.getMillis());
		String result = "";
		//kde kdy co
		result += record.getSourceClassName() + " "
				+ record.getSourceMethodName() + " "
				+ record.getParameters();
		result += Os.getNewLine();
		result += d.toString();
		result += Os.getNewLine();
		
		return result + makeMessage(record);
	}
	
	private String makeMessage(LogRecord record) {
		return record.getLevel() + ": " + record.getMessage();
	}
}
