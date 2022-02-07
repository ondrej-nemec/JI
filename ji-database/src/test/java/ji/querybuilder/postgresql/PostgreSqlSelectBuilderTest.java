package ji.querybuilder.postgresql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.enums.Join;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class PostgreSqlSelectBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		Connection mock = mock(Connection.class);
		SelectBuilder builder = new PostgreSqlSelectBuilder(mock, "a.id", "a.name", "a.FK_id")
					.from("table_name a")
					.join("joined_table b", Join.INNER_JOIN, "a.id = b.id")
					.where("b.id = 1")
					.andWhere("b.name = %name")
					.addParameter("%name", "name")
					.orWhere("b.name = %nname")
					.addParameter("%nname", "nname")
					.groupBy("a.id")
					.orderBy("a.id DESC")
					.having("a.id > %id")
					.addParameter("%id", 10)
					.limit(0, 0);
		
		String expected = "SELECT a.id, a.name, a.FK_id"
				+ " FROM table_name a"
				+ " JOIN joined_table b ON a.id = b.id"
				+ " WHERE (b.id = 1)"
				+ " AND (b.name = %name)"
				+ " OR (b.name = %nname)"
				+ " GROUP BY a.id"
				+ " ORDER BY a.id DESC"
				+ " HAVING a.id > %id"
				+ " LIMIT 0"
				+ " OFFSET 0";
		
		String created = "SELECT a.id, a.name, a.FK_id"
				+ " FROM table_name a"
				+ " JOIN joined_table b ON a.id = b.id"
				+ " WHERE (b.id = 1)"
				+ " AND (b.name = 'name')"
				+ " OR (b.name = 'nname')"
				+ " GROUP BY a.id"
				+ " ORDER BY a.id DESC"
				+ " HAVING a.id > 10"
				+ " LIMIT 0"
				+ " OFFSET 0";
		
		assertEquals(expected, builder.getSql());
		assertEquals(created, builder.createSql());
		verifyNoMoreInteractions(mock);
	}
	
}
