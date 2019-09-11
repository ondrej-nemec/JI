package common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.exceptions.LogicException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ImplodeTest {

	@Test(expected = LogicException.class)
	@Parameters(method = "dataImplodeThrowsIfDataInfalid")
	public void testImplodeThrowsIfDataInfalid(String[] data, int from, int to) {
		Implode.implode("", data, from, to);
	}
	
	public Object[] dataImplodeThrowsIfDataInfalid() {
		String[] array = new String[] {"a", "b", "c","d"};
		return new Object[] {
			new Object[] {
				array, -1, 2
			},
			new Object[] {
				array, 1, 8
			},
			new Object[] {
				array, 1, -2
			},
			new Object[] {
				array, 3, 1
			},
			new Object[] {
				array, 8, 2
			},
		};
	}
	
	@Test
	@Parameters(method = "dataImplodeReturnsCorrectResult")
	public void testImplodeReturnsCorrectResult(String expected, String glue, String[] data, int from, int to) {
		assertEquals(expected, Implode.implode(glue, data, from, to));
	}
	
	public Object[] dataImplodeReturnsCorrectResult() {
		String[] array = new String[] {"a", "b", "c","d"};
		return new Object[] {
			new Object[] {
				"a b c d", " ", array, 0, 3
			},
			new Object[] {
					"a, b, c, d", ", ", array, 0, 3
			},
			new Object[] {
					"b c d", " ", array, 1, 3
			},
			new Object[] {
					"b c", " ", array, 1, 2
			},
		};
	}
	
}
