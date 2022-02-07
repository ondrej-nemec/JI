package ji.querybuilder.derby;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DerbyFunctionsTest {

	@Test
	public void testConcat() {
		assertEquals("(aa || bb || cc)", new DerbyFunctions().concat("aa", "bb", "cc"));
	}
	
}
