package common;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TerminalTest {

	@Test
	@Parameters
	public void testRunWorks(String command) {
		fail("Not yet implemented");
	}
	
	public Collection<Object[]> parametersForTestRunWorks() {
		return Arrays.asList(
				new Object[]{},
				new Object[]{}
			);
	}

}
