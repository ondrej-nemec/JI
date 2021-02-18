package querybuilder;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class ParametersTest {

	@Test
	@junitparams.Parameters(method = "dataAddParameter")
	public void testAddParameter(Object value, String expected) {
		Parameters<String> p = new Parameters<String>() {
			@Override public String getSql() {return null;}
			@Override public String createSql() {return null;}
			@Override public String addNotEscapedParameter(String name, String value) {return value;}
		};
		assertEquals(expected, p.addParameter("parameter", value));
	}
	
	public Object[] dataAddParameter() {
		return new Object[] {
			new Object[] {null, "null"},
			new Object[] {false, "false"},
			new Object[] {new Boolean(true), "true"},
			new Object[] {1, "1"},
			new Object[] {new Double(123.4), "123.4"},
			new Object[] {'c', "'c'"},
			new Object[] {(byte)42, "42"},
			new Object[] {"", "''"},
			new Object[] {"some text", "'some text'"},
			new Object[] {
					Arrays.asList("a", "b", "c"),
					"'a','b','c'"
				}
		};
	}
	
}
