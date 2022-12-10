package ji.common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public class Log4j2LoggerTestImpl implements Logger {
	
	private final String file;
	private final String name;
	
	public Log4j2LoggerTestImpl(String name) {
		this(name, null);
	}
	
	public Log4j2LoggerTestImpl(String name, String file) {
		this.file = file == null ? null : file.replace(
			".", 
			LocalDateTime.now().toString().replace(".", "-").replace(":", "-") + "."
		);
		this.name = name;
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

    private void print(String severity, Object message, Throwable t) {
    	StackTraceElement trace = null;
    	for (StackTraceElement el : Thread.currentThread().getStackTrace()) {
    		if (!el.getClassName().equals(Thread.class.getName()) && !el.getClassName().equals(getClass().getName())) {
    			trace = el;
    			break;
    		}
    	}
    	StringBuilder text = new StringBuilder(String.format(
				"%s %s [%s]%s (%s:%s) %s",
				LocalDateTime.now().toString().replace("T", " "),
				severity,
				Thread.currentThread().getName(),
				name == null ? "" : " - " + name,
				trace == null ? "" : trace.getFileName(),
				trace == null ? "" : trace.getLineNumber(),
				message.toString()
		));
    	if (t != null) {
    		Throwable ext = t;
    		while(ext != null) {
    			text.append("\n\t");
    			text.append(String.format("%s: %s", ext.getClass(), ext.getMessage()));
    			for (StackTraceElement ste : ext.getStackTrace()) {
        			text.append("\n\t");
        			text.append(String.format(
        				"at %s.%s (%s:%s)", 
        				ste.getClassName(),
        				ste.getMethodName(),
        				ste.getFileName(),
        				ste.getLineNumber()
        			));
        		}
        		ext = ext.getCause();
        		if (ext != null) {
            		text.append("\n\tCaused:");
        		}
    		}
    	}
		System.out.println(text);
		if (file != null) {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
				bw.write(text.toString());
				bw.write("\n");
				bw.flush();
			} catch (Exception e) {}
		}
	}
	
	private void print(String severity, Object message) {
		print(severity, message.toString(), null);
	}

	@Override
	public void catching(Level level, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void catching(Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message) {
		print("DEBUG", message);
	}

	@Override
	public void debug(String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Throwable throwable) {
		print("DEBUG", message, throwable);
	}

	@Override
	public void debug(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2) {
		print(
			"DEBUG",
			message
				.replaceFirst("\\{\\}", p0.toString())
				.replaceFirst("\\{\\}", p1.toString())
				.replaceFirst("\\{\\}", p2.toString())
		);
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void entry() {
		throw new NotImplementedException();
		
	}

	@Override
	public void entry(Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message) {
		print("ERROR", message);
	}

	@Override
	public void error(String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Throwable throwable) {
		print("ERROR", message, throwable);
	}

	@Override
	public void error(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void exit() {
		throw new NotImplementedException();
		
	}

	@Override
	public <R> R exit(R result) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(CharSequence message) {
		print("FATAL", message);
	}

	@Override
	public void fatal(CharSequence message, Throwable throwable) {
		print("FATAL", message, throwable);
	}

	@Override
	public void fatal(String message) {
		print("FATAL", message);
	}

	@Override
	public void fatal(String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Throwable throwable) {
		print("FATAL", message, throwable);
	}

	@Override
	public void fatal(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public Level getLevel() {
		throw new NotImplementedException();
		
	}

	@Override
	public <MF extends MessageFactory> MF getMessageFactory() {
		throw new NotImplementedException();
		
	}

	@Override
	public String getName() {
		return name;
		
	}

	@Override
	public void info(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(CharSequence message) {
		print("INFO", message);
	}

	@Override
	public void info(CharSequence message, Throwable throwable) {
		print("INFO", message, throwable);
	}

	@Override
	public void info(String message) {
		print("INFO", message);
	}

	@Override
	public void info(String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Throwable throwable) {
		print("INFO", message, throwable);
	}

	@Override
	public void info(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return true;
	}

	@Override
	public boolean isEnabled(Level level) {
		return true;
	}

	@Override
	public boolean isEnabled(Level level, Marker marker) {
		return true;
	}

	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return true;
	}

	@Override
	public boolean isFatalEnabled() {
		return true;
	}

	@Override
	public boolean isFatalEnabled(Marker marker) {
		return true;
	}

	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return true;
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return true;
	}

	@Override
	public void log(Level level, Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
			Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
			Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
			Object p5, Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
			Object p5, Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4,
			Object p5, Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void printf(Level level, Marker marker, String format, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void printf(Level level, String format, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public <T extends Throwable> T throwing(Level level, T throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public <T extends Throwable> T throwing(T throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(CharSequence message) {
		print("TRACE", message);
	}

	@Override
	public void trace(CharSequence message, Throwable throwable) {
		print("TRACE", message, throwable);
	}

	@Override
	public void trace(String message) {
		print("TRACE", message);
	}

	@Override
	public void trace(String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Throwable throwable) {
		print("TRACE", message, throwable);
	}

	@Override
	public void trace(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();
		
	}

	@Override
	public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
		
	}

	@Override
	public EntryMessage traceEntry() {
		throw new NotImplementedException();
		
	}

	@Override
	public EntryMessage traceEntry(String format, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public EntryMessage traceEntry(Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void traceExit() {
		throw new NotImplementedException();
		
	}

	@Override
	public <R> R traceExit(R result) {
		throw new NotImplementedException();
	}

	@Override
	public <R> R traceExit(String format, R result) {
		throw new NotImplementedException();
	}

	@Override
	public void traceExit(EntryMessage message) {
		throw new NotImplementedException();
		
	}

	@Override
	public <R> R traceExit(EntryMessage message, R result) {
		throw new NotImplementedException();
	}

	@Override
	public <R> R traceExit(Message message, R result) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, Message message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, Message message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, MessageSupplier messageSupplier) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, CharSequence message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, CharSequence message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, Object message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, Object message, Throwable throwable) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, String message) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, String message, Object... params) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
		
	}

	@Override
	public void warn(Marker marker, String message, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, Supplier<?> messageSupplier) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Message message) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Message message, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(MessageSupplier messageSupplier) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(MessageSupplier messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(CharSequence message) {
		print("WARN", message);
	}

	@Override
	public void warn(CharSequence message, Throwable throwable) {
		print("WARN", message);
	}

	@Override
	public void warn(String message) {
		print("WARN", message);
	}

	@Override
	public void warn(String message, Object... params) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Supplier<?>... paramSuppliers) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Throwable throwable) {
		print("WARN", message, throwable);
	}

	@Override
	public void warn(Supplier<?> messageSupplier) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Supplier<?> messageSupplier, Throwable throwable) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5,
			Object p6, Object p7, Object p8, Object p9) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		throw new NotImplementedException();	
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7) {
		throw new NotImplementedException();
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8) {
		throw new NotImplementedException();		
	}

	@Override
	public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6,
			Object p7, Object p8, Object p9) {
		throw new NotImplementedException();		
	}
}
