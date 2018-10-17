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
	
	private String pre = "cmd /c "; //TODO depend on OS
		
	private String path = 
			"src" +
			File.separator +
			"test" +
			File.separator +
			"resource" +
			File.separator +
			"terminaltest" +
			File.separator;
	
	private String exc = ".bat"; //TODO depend on OS
	
	@Test
	@Parameters
	public void testRunWorks(String command, int expectedCode) {
		Terminal terminal = new Terminal(mock(Logger.class));
		
		int code = terminal.run((a)->{}, (a)->{}, command);
		
		assertEquals(expectedCode, code);		
	}
	
	public Collection<Object[]> parametersForTestRunWorks() {
		return Arrays.asList(
				new Object[]{ //working void command
					pre + "exit", 0,
				},
				new Object[]{ // not working command
					"notExisting", -1,
				},
				new Object[]{
					path + "success" + exc, 0,
				},
				new Object[]{
					path + "bad-command" + exc, 1, 
				}
			);
	}

}
