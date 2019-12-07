package querybuilder.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.Join;

@RunWith(JUnitParamsRunner.class)
public class EnumToStringTest {

	@Test
	@Parameters
	public void testJoinToString(Join join, String expected) {
		assertEquals(expected, EnumToMysqlString.joinToString(join));
	}
	
	public Object[] parametersForTestJoinToString() {
		return new Object[] {
			new Object[] {Join.INNER_JOIN, "JOIN"},
			new Object[] {Join.LEFT_OUTER_JOIN, "LEFT JOIN"},
			new Object[] {Join.RIGHT_OUTER_JOIN, "RIGHT JOIN"}
		};
	}
	
	@Test(expected=RuntimeException.class)
	public void testJoinToStringThrowsWhenFullOuterJoin() {
		EnumToMysqlString.joinToString(Join.FULL_OUTER_JOIN);
	}
	
	@Test
	public void testSettingToStringReturnsCorrectString() {
		fail("Not finished");
	}
	
	@Test
	public void testTypeToStringReturnsCorrectString() {
		fail("Not finished");
	}
}
