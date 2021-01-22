package querybuilder.postgresql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PostgreSqlFunctionsTest {

	@Test
	public void testConcat() {
		assertEquals("CONCAT(aa, bb, cc)", new PostgeSqlFunctions().concat("aa", "bb", "cc"));
	}
	
}
