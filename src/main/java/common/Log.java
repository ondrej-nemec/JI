package common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import text.plaintext.PlainTextCreator;

public class Log {
	
	public static void setLogger(Logger l) {
		l.setLevel(Level.ALL);	
		
		String fileName = "";
		if (l.getName() == null)
			fileName = "logs/_default.log";
		else
			fileName = "logs/" + l.getName() + ".log";
		
		l.addHandler(consoleHandler());
		l.addHandler(fileHandler(fileName));
	}
	
	private static Handler fileHandler(String path) {
		return new Handler() {
			
			private PlainTextCreator c = new PlainTextCreator();
			
			private List<LogRecord> front = new LinkedList<>();
			
			@Override
			public Level getLevel() {
				return Level.ALL;
			}
			
			@Override
			public void publish(LogRecord record) {
				try(BufferedWriter bw = c.buffer(path, true)) {
					for (LogRecord rec : front) {
						c.write(bw, makeMessage(rec));
						front.remove(rec);
					}					
					c.write(bw, makeMessage(record));
				} catch (IOException e) {
					front.add(record);
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
	
	private static Handler consoleHandler () {
		return new Handler() {
						
			@Override
			public Level getLevel() {
				return Level.ALL;
			}
			
			@Override
			public synchronized void publish(LogRecord record) {
				if (record.getLevel().intValue() >= Level.CONFIG.intValue())
					Console.err(makeMessage(record));
				else
					Console.out(makeMessage(record));
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
	
	private static String makeMessage(LogRecord record) {
		return record.getLevel() + ": " + record.getMessage(); //TODO format message
	}
}
