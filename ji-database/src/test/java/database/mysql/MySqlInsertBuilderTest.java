package database.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;

import database.support.DoubleConsumer;

public class MySqlInsertBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlInsertBuilder(mock, "table_name")
					.addValue("column1", "value1")
					.addValue("column2", "value2")
					.getSql();
		
		String expected = "INSERT INTO table_name"
				+ " (column1, column2)"
				+ " VALUES"
				+ " (?, ?)";
		
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}
	
}
