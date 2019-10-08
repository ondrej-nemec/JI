package logging.loggers;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import logging.LogLevel;
import logging.LoggerConfig;

public class Log4JLogger implements common.Logger {
	
	private final Logger log;
	
	public Log4JLogger(final String name, final LoggerConfig config) {
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		Priority priority = levelToPriority(config.getMinLogLevel());
		
		// TODO switch medium
		Logger.getRootLogger().addAppender(createConsoleAppender(priority));
		Logger.getRootLogger().addAppender(createFileAppender(priority, config.getPathToLogs() + "/" + name + "/" + name + ".log"));
		
		this.log = Logger.getLogger(name);
	}

	@Override
	public void debug(Object message) {
		log.debug(message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		log.debug(message, t);
	}

	@Override
	public void info(Object message) {
		log.info(message);
	}

	@Override
	public void info(Object message, Throwable t) {
		log.info(message, t);
	}

	@Override
	public void warn(Object message) {
		log.warn(message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		log.warn(message, t);
	}

	@Override
	public void error(Object message) {
		log.error(message);
	}

	@Override
	public void error(Object message, Throwable t) {
		log.error(message, t);
	}

	@Override
	public void fatal(Object message) {
		log.fatal(message);
	}

	@Override
	public void fatal(Object message, Throwable t) {
		log.fatal(message, t);
	}
	
	/************* APPENDERS **********************/
	
	private ConsoleAppender createConsoleAppender(final Priority priority) {
		//TODO two appenders - err a out
		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout("%p %d{yyyy-MM-dd HH:mm:ss} %c{3} [%t] - %m%n"));
		console.setThreshold(priority);
		console.setTarget(ConsoleAppender.SYSTEM_OUT);
		console.activateOptions();
		return console;
	}
	
	private FileAppender createFileAppender(final Priority priority, final String pathToLogs) {
		FileAppender file = new DailyRollingFileAppender();
		file.setFile(pathToLogs);
		file.setLayout(new PatternLayout("%p %d{yyyy-MM-dd HH:mm:ss} [%t] - %m%n")); // %c{3}:%L - jmeno loggeru:radek
		file.setThreshold(priority);
		file.setAppend(true);
		file.activateOptions();
		return file;
	}
	
	//TODO
	/*
	private SMTPAppender createSmtpAppender() {
		SMTPAppender email = new SMTPAppender();
		return email;
	}
*/
	
	/****************************************************/

	private Priority levelToPriority(LogLevel minLogLevel) {
		switch (minLogLevel) {
    		case NO_LOG: return Level.OFF;
    		case FATAL: return Level.FATAL;
    		case ERROR: return Level.ERROR;
    		case WARNING: return Level.WARN;
    		case INFO: return Level.INFO;
    		case DEBUG: return Level.DEBUG;
    		default: return Level.ALL;
		}
	}
}
