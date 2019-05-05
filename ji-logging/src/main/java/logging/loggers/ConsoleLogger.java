package logging.loggers;

import common.Console;
import common.Logger;

public class ConsoleLogger implements Logger {
	
	private final String name;
	
	private final Console console;
	
	public ConsoleLogger(final String name) {
		this.name = name;
		this.console = new Console();
	}

	@Override
	public void debug(Object message) {
		print("DEBUG", message.toString());
	}

	@Override
	public void debug(Object message, Throwable t) {
		print("DEBUG", message.toString(), t);
	}

	@Override
	public void info(Object message) {
		print("INFO", message.toString());
	}

	@Override
	public void info(Object message, Throwable t) {
		print("INFO", message.toString(), t);
	}

	@Override
	public void warn(Object message) {
		print("WARNING", message.toString());
	}

	@Override
	public void warn(Object message, Throwable t) {
		print("WARNING", message.toString(), t);
	}

	@Override
	public void error(Object message) {
		print("ERROR", message.toString());
	}

	@Override
	public void error(Object message, Throwable t) {
		print("ERROR", message.toString(), t);
	}

	@Override
	public void fatal(Object message) {
		print("FATAL", message.toString());
	}

	@Override
	public void fatal(Object message, Throwable t) {
		print("FATAL", message.toString(), t);
	}
	
	private void print(final String level, final String message, final Throwable t) {
		print(level, message + " " + t.getClass() + " " + t.getMessage());
	}
	
	private void print(final String level, final String message) {
		console.out(level + "[" + name + "]: " + message);
	}

}
