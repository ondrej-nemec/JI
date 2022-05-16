package ji.socketCommunication;

import java.time.LocalDateTime;

import ji.common.Logger;

public class LoggerImpl implements Logger {

    public void print(String severity, Object message) {
    	StackTraceElement trace = null;
    	for (StackTraceElement el : Thread.currentThread().getStackTrace()) {
    		if (!el.getClassName().equals(Thread.class.getName()) && !el.getClassName().equals(LoggerImpl.class.getName())) {
    			trace = el;
    			break;
    		}
    	}
		System.out.println(
			String.format(
				"%s %s [%s] (%s:%s) %s",
				LocalDateTime.now().toString().replace("T", " "),
				severity,
				Thread.currentThread().getName(),
				trace == null ? "" : trace.getFileName(),
				trace == null ? "" : trace.getLineNumber(),
				message.toString())
		);
	}
	
	public void print(String severity, Object message, Throwable t) {
		/*
		print(severity, message.toString() + ", " + t.getMessage());
		/*/
		print(severity, message.toString());
		t.printStackTrace();
		//*/
	}

	@Override
	public void debug(Object message) {
		print("DEBUG", message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		print("DEBUG", message, t);
	}

	@Override
	public void info(Object message) {
		print("INFO", message);
	}

	@Override
	public void info(Object message, Throwable t) {
		print("INFO", message, t);
	}

	@Override
	public void warn(Object message) {
		print("WARN", message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		print("WARN", message, t);
	}

	@Override
	public void error(Object message) {
		print("ERROR", message);
	}

	@Override
	public void error(Object message, Throwable t) {
		print("ERROR", message, t);
	}

	@Override
	public void fatal(Object message) {
		print("FATAL", message);
	}

	@Override
	public void fatal(Object message, Throwable t) {
		print("FATAL", message, t);
	}

	@Override
	public void trace(Object message) {
		print("TRACE", message);
	}

	@Override
	public void trace(Object message, Throwable t) {
		print("TRACE", message, t);
	}

}
