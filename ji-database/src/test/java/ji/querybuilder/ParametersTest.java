package ji.querybuilder;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.querybuilder.buildersparent.ParametrizedBuilder;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class ParametersTest {

	@Test
	@junitparams.Parameters(method = "dataAddParameter")
	public void testAddParameter(Object value, String expected) {
		ParametrizedBuilder<String> p = new ParametrizedBuilder<String>() {
			@Override public String getSql() { return null; }
			@Override public Map<String, String> getParameters() { return null; }
			@Override public String _addNotEscapedParameter(String name, String value) { return value; }
		};
		assertEquals(expected, p.addParameter("parameter", value));
	}
	
	public Object[] dataAddParameter() {
		return new Object[] {
			new Object[] {null, "null"},
			new Object[] {false, "false"},
			new Object[] {Boolean.valueOf(true), "true"},
			new Object[] {1, "1"},
			new Object[] {Double.valueOf(123.4), "123.4"},
			new Object[] {'c', "'c'"},
			new Object[] {(byte)42, "42"},
			new Object[] {"", "''"},
			new Object[] {"some text", "'some text'"},
			new Object[] {
				Arrays.asList("a", "b", "c"),
				"'a','b','c'"
			},
			new Object[] {LocalDateTime.of(2021, 10, 23, 19, 18), "'2021-10-23 19:18'"}
		};
	}
	
}
