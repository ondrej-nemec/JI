package database.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;

import database.support.DoubleConsumer;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.Join;

@RunWith(JUnitParamsRunner.class)
public class MySqlSelectBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlSelectBuilder(mock, "a.id, a.name, a.FK_id")
					.from("table_name a")
					.join("joined_table b", Join.INNER_JOIN, "a.id = b.id")
					.where("b.id = 1")
					.andWhere("b.name = 'name'")
					.orWhere("b.name = 'nname'")
					.groupBy("a.id")
					.orderBy("a.id DESC")
					.having("a.id > 10")
					.limit(0)
					.offset(0)
					.getSql();
		
		String expected = "SELECT a.id, a.name, a.FK_id"
				+ " FROM table_name a"
				+ " JOIN joined_table b ON a.id = b.id"
				+ " WHERE b.id = 1"
				+ " AND b.name = 'name'"
				+ " OR (b.name = 'nname')"
				+ " GROUP BY a.id"
				+ " ORDER BY a.id DESC"
				+ " HAVING a.id > 10"
				+ " LIMIT 0"
				+ " OFFSET 0";
		
		// TODO add params test
		System.out.println("TODO select");
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}
	
	@Test
	@Parameters
	public void testJoinToString(Join join, String expected) {
		assertEquals(
			expected,
			new MySqlSelectBuilder(null, "").joinToString(join)
		);
	}
	
	@Test(expected=RuntimeException.class)
	public void testJoinToStringThrowsWhenFullOuterJoin() {
		new MySqlSelectBuilder(null, "").joinToString(Join.FULL_OUTER_JOIN);
	}
	
	public Object[] parametersForTestJoinToString() {
		return new Object[] {
			new Object[] {Join.INNER_JOIN, "JOIN"},
			new Object[] {Join.LEFT_OUTER_JOIN, "LEFT JOIN"},
			new Object[] {Join.RIGHT_OUTER_JOIN, "RIGHT JOIN"}
		};
	}
	
}
