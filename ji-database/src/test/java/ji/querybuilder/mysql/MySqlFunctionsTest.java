package ji.querybuilder.mysql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ji.querybuilder.mysql.MySqlFunctions;

public class MySqlFunctionsTest {

	@Test
	public void testConcat() {
		assertEquals("CONCAT(aa, bb, cc)", new MySqlFunctions().concat("aa", "bb", "cc"));
	}
	
}
