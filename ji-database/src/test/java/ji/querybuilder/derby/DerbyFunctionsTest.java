package ji.querybuilder.derby;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ji.querybuilder.derby.DerbyFunctions;

public class DerbyFunctionsTest {

	@Test
	public void testConcat() {
		assertEquals("(aa || bb || cc)", new DerbyFunctions().concat("aa", "bb", "cc"));
	}
	
}
