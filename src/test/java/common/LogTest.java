package common;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class LogTest {

	@Test
	public void testSetLoggerWorks() {
		Logger logger = Logger.getAnonymousLogger();
		Log log = new Log(new Console(), "logs/");
		
		log.addAllHandlers(logger);
		
		assertEquals(Level.ALL, logger.getLevel());
		assertFalse(logger.getUseParentHandlers());
		assertEquals(2, logger.getHandlers().length);
	}	

	@Test
	@Parameters
	@Ignore("make end to end test")
	public void testFileHandlerWorks(String loggerName, Level level, String message, String expectedFileName, String expectedMessage) throws IOException {
		Handler handler = new Log(null, "logs/").fileHandler(loggerName);
		
		LogRecord record = new LogRecord(level, message);
		record.setMillis(0);
		handler.publish(record);
		
	}
	
	public Collection<Object[]> parametersForTestFileHandlerWorks() {
		return Arrays.asList(
				 new Object[] {
						 "logger", Level.FINEST, "finest message", "logger", "FINEST: finest message"
				 },
				 new Object[] {
						 "logger", Level.FINER, "finer message", "logger", "FINER: finer message"
				 },
				 new Object[] {
						 "logger", Level.FINE, "fine message", "logger", "FINE: fine message"
				 },
				 new Object[] {
						 "logger", Level.CONFIG, "config message", "logger", "CONFIG: config message"
				 },
				 new Object[] {
						 "logger", Level.INFO, "info message", "logger", "INFO: info message"
				 },
				 new Object[] {
						 "logger", Level.WARNING, "warning message", "logger", "WARNING: warning message"
				 },
				 new Object[] {
						 "logger", Level.SEVERE, "severe message", "logger", "SEVERE: severe message"
				 },
				 new Object[] {
						 null, Level.FINEST, "finest message", "_default", "FINEST: finest message"
				 },
				 new Object[] {
						 null, Level.FINER, "finer message", "_default", "FINER: finer message"
				 },
				 new Object[] {
						 null, Level.FINE, "fine message", "_default", "FINE: fine message"
				 },
				 new Object[] {
						 null, Level.CONFIG, "config message", "_default", "CONFIG: config message"
				 },
				 new Object[] {
						 null, Level.INFO, "info message", "_default", "INFO: info message"
				 },
				 new Object[] {
						 null, Level.WARNING, "warning message", "_default", "WARNING: warning message"
				 },
				 new Object[] {
						 null, Level.SEVERE, "severe message", "_default", "SEVERE: severe message"
				 }
			);
	}

	@Test
	@Parameters
	public void testConsoleHandlerWorks(Level level, String message, String expectedMessage, boolean isErr) {
		Console console = mock(Console.class);
		Handler handler = new Log(console, "logs").consoleHandler();
		
		LogRecord record = new LogRecord(level, message);
		record.setMillis(0);
		handler.publish(record);
		
		if(isErr)
			verify(console, times(1)).err(getMessage(expectedMessage));
		else
			verify(console, times(1)).out(getMessage(expectedMessage));
		verifyNoMoreInteractions(console);
	}
	
	public Collection<Object[]> parametersForTestConsoleHandlerWorks() {
		return Arrays.asList(
				 new Object[] {
						Level.FINEST, "finest message", "FINEST: finest message", false 
				 },
				 new Object[] {
						 Level.FINER, "finer message", "FINER: finer message", false 
				 },
				 new Object[] {
						 Level.FINE, "fine message", "FINE: fine message", false 
				 },
				 new Object[] {
						 Level.CONFIG, "config message", "CONFIG: config message", true 
				 },
				 new Object[] {
						 Level.INFO, "info message", "INFO: info message", true 
				 },
				 new Object[] {
						 Level.WARNING, "warning message", "WARNING: warning message", true 
				 },
				 new Object[] {
						 Level.SEVERE, "severe message", "SEVERE: severe message", true 
				 }
			);
	}
	
	private String getMessage(String expect) {
		return "Thu Jan 01 01:00:00 CET 1970 | null : null : null" + Os.getNewLine() + expect;
	}
}
