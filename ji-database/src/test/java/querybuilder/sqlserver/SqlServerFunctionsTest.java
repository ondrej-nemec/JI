package querybuilder.sqlserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SqlServerFunctionsTest {

	@Test
	public void testConcat() {
		assertEquals("CONCAT(aa, bb, cc)", new SqlServerFunctions().concat("aa", "bb", "cc"));
	}
	
}
