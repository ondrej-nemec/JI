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

public class Log4JLogger extends Logger implements common.Logger {
			
	public Log4JLogger(String name, LoggerConfig config) {
		super(name);
		this.repository = Logger.getRootLogger().getLoggerRepository();
		
		Level priority = levelToPriority(config.getLogLevel(name));
		setLevel(priority);
		
		config.getTypes(name).forEach((type)->{
			switch (type) {
			case FILE:
				String path = "";
				String rootPath = config.getLoggerPath(name);
				String typePath = config.getLoggerPath(name, type);
				if (rootPath.equals(typePath)) {
					path = rootPath + "/" + name + "/" + name + ".log";
				} else {
					path = typePath;
				}
				addAppender(createFileAppender(
						levelToPriority(config.getLogLevel(name, type)),
						path,
						config.getMessagePattern(name, type))
				);
				break;
			case CONSOLE:
				addAppender(createConsoleAppender(
						levelToPriority(config.getLogLevel(name, type)),
						name, 
						config.getMessagePattern(name, type))
				);
			default:
				break;
			}
		});
	}

	/************* APPENDERS **********************/
	
	private ConsoleAppender createConsoleAppender(final Priority priority, String name, String pattern) {
		//TODO two appenders - err a out
		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(pattern));
		console.setThreshold(priority);
		console.setTarget(ConsoleAppender.SYSTEM_OUT);
		console.activateOptions();
		return console;
	}
	
	private FileAppender createFileAppender(final Priority priority, final String pathToLogs, String pattern) {
		FileAppender file = new DailyRollingFileAppender();
		file.setFile(pathToLogs);
		file.setLayout(new PatternLayout(pattern));
		file.setThreshold(priority);
		file.setAppend(true);
		file.activateOptions();
		return file;
	}
	
	//TODO - email servers
	/*
	private SMTPAppender createSmtpAppender() {
		SMTPAppender email = new SMTPAppender();
		return email;
	}
*/
	
	/****************************************************/

	private Level levelToPriority(LogLevel minLogLevel) {
		switch (minLogLevel) {
    		case NO_LOG: return Level.OFF;
    		case FATAL: return Level.FATAL;
    		case ERROR: return Level.ERROR;
    		case WARNING: return Level.WARN;
    		case INFO: return Level.INFO;
    		case DEBUG: return Level.DEBUG;
    		case ALL:
    		default: return Level.ALL;
		}
	}
}
