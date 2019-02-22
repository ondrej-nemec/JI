package common;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TerminalTest {
	
	private String path = 
			"src" +
			Os.getPathSeparator() +
			"test" +
			Os.getPathSeparator() +
			"resource" +
			Os.getPathSeparator() +
			"terminal" +
			File.separator;
	
	@Test
	@Parameters
	public void testRunWorks(final String command, int expectedCode) {
		Terminal terminal = new Terminal(mock(Logger.class));
		
		int code = terminal.run((a)->{}, (a)->{}, command);
		
		assertEquals(expectedCode, code);		
	}
	
	public Collection<Object[]> parametersForTestRunWorks() {
		return Arrays.asList(
				new Object[]{ //working void command
					Os.getPreCommand() + "exit", 0,
				},
				new Object[]{ //working void command
					Os.getPreCommand() + "echo success", 0,
				},
				new Object[]{ // not working command
					"notExisting", -1,
				},
				new Object[]{
					path + "success" + Os.getCliExtention(), 0,
				},
				new Object[]{
					path + "bad-command" + Os.getCliExtention(), 1, 
				}
			);
	}

}
