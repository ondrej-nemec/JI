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
					.addColumns("column1", "column2")
					.values(":value1", ":value2")
					.getSql();
		
		String expected = "INSERT INTO table_name"
				+ " (column1, column2)"
				+ " VALUES"
				+ " (:value1, :value2)";
		
		// TODO add params test
		System.out.println("TODO insert");
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}
	
}
