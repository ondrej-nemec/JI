package logging.loggers;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

import common.ILogger;
import common.env.AppMode;

public class Log4JLogger extends Logger implements ILogger {
	
	public Log4JLogger(final String name, final AppMode mode, final String pathToLogs) {
		super(name);
		clearConfiguration();
		Priority priority = null;
		switch (mode) {
		case TEST: priority = Level.OFF; break;
		case DEV: priority = Level.ALL; break;
		case PROD: priority = Level.INFO; break;
			default: throw new RuntimeException("Unsupported app mode " + mode);
		}
		Logger.getRootLogger().addAppender(createConsoleAppender(priority));
		Logger.getRootLogger().addAppender(createFileAppender(priority, pathToLogs));
	}
	
	public static void clearConfiguration() {
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
	}

	@Override
	public void debug(Object message) {
		super.debug(message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		super.debug(message, t);
	}

	@Override
	public void info(Object message) {
		super.info(message);
	}

	@Override
	public void info(Object message, Throwable t) {
		super.info(message, t);
	}

	@Override
	public void warn(Object message) {
		super.warn(message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		super.warn(message, t);
	}

	@Override
	public void error(Object message) {
		super.error(message);
	}

	@Override
	public void error(Object message, Throwable t) {
		super.error(message, t);
	}

	@Override
	public void fatal(Object message) {
		super.fatal(message);
	}

	@Override
	public void fatal(Object message, Throwable t) {
		super.fatal(message, t);
	}
	
	private ConsoleAppender createConsoleAppender(final Priority priority) {
		//TODO two appenders - err a out
		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout("%p %d{yyyy-MM-dd HH:mm:ss} %c{3}:%L [%t] - %m%n"));
		console.setThreshold(priority);
		console.setTarget(ConsoleAppender.SYSTEM_OUT);
		console.activateOptions();
		return console;
	}
	
	private FileAppender createFileAppender(final Priority priority, final String pathToLogs) {
		FileAppender file = new DailyRollingFileAppender();
		file.setFile(pathToLogs);
		file.setLayout(new PatternLayout("%p %d{yyyy-MM-dd HH:mm:ss} %c{3}:%L [%t] - %m%n"));
		file.setThreshold(priority);
		file.setAppend(true);
		file.activateOptions();
		return null;
	}
	
	//TODO
	/*
	private SMTPAppender createSmtpAppender() {
		SMTPAppender email = new SMTPAppender();
		return email;
	}
*/
}
