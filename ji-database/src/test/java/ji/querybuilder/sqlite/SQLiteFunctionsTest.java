package ji.querybuilder.sqlite;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SQLiteFunctionsTest {

	@Test
	public void testConcat() {
		// TODO tohle je potreba upravit
		assertEquals("CONCAT(aa, bb, cc)", new SQLiteFunctions().concat("aa", "bb", "cc"));
	}
	
}
