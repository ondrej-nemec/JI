package logging.loggers;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogFactory;

import utils.env.LoggerConfig;

public class Log4JLogger implements common.Logger, Log {
	
	private final Logger log;
	
	private final boolean isDebugEnabled;
	
	public static Log4JLogger createLog4JLogger(final String name, final LoggerConfig config) {
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		
		Log4JLogger log = new Log4JLogger(name, config);
		LogFactory.setLogCreator((clazz)->{
			return log;
		});
		return log;
	}
	
	private Log4JLogger(final String name, final LoggerConfig config) {
		Priority priority = null;
		switch (config.getAppMode()) {
			case TEST:
				priority = Level.OFF;
				this.isDebugEnabled = false;
				break;
			case DEV:
				priority = Level.ALL;
				this.isDebugEnabled = true;
				break;
			case PROD:
				priority = Level.INFO;
				this.isDebugEnabled = false;
				break;
			default: throw new RuntimeException("Unsupported app mode " + config.getAppMode());
		}
		Logger.getRootLogger().addAppender(createConsoleAppender(priority));
		Logger.getRootLogger().addAppender(createFileAppender(priority, config.getPathToLogs()));
		
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

	@Override
	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	@Override
	public void debug(String message) {
		debug((Object)message);
	}

	@Override
	public void info(String message) {
		info((Object)message);
	}

	@Override
	public void warn(String message) {
		warn((Object)message);
	}

	@Override
	public void error(String message) {
		error((Object)message);
	}

	@Override
	public void error(String message, Exception e) {
		error((Object)message, e);
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
		return file;
	}
	
	//TODO
	/*
	private SMTPAppender createSmtpAppender() {
		SMTPAppender email = new SMTPAppender();
		return email;
	}
*/
}
