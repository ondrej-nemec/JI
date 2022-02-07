package ji.querybuilder.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;

import ji.querybuilder.builders.InsertBuilder;

public class SqlServerInsertBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		Connection mock = mock(Connection.class);
		InsertBuilder builder = new SqlServerInsertBuilder(mock, "table_name")
					.addValue("column1", "value1")
					.addValue("column2", 1);
		
		String expected = "INSERT INTO table_name"
				+ " (column1, column2)"
				+ " VALUES"
				+ " ('value1', 1)";
		
		assertEquals(expected, builder.getSql());
	//	assertEquals(created, builder.createSql());
		verifyNoMoreInteractions(mock);
	}
	
}
