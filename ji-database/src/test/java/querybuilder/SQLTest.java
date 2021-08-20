package querybuilder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.enums.SQL;

@RunWith(JUnitParamsRunner.class)
public class SQLTest {

	@Test
	@Parameters(method = "dataEscape")
	public void testEscape(String expected, String sql) {
		assertEquals(expected, SQL.escape(sql));
	}
	
	public Object[] dataEscape() {
		return new Object[] {
			new Object[] {
				"not-escaped", "not-escaped"
			},
			new Object[] {
				"single''quote", "single'quote"
			}
		};
	}
}
