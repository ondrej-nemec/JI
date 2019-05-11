package database.mysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import database.support.DoubleConsumer;

public class MySqlDeleteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlDeleteBuilder(mock, "table_name")
					.where("id > 1")
					.andWhere("id < ?")
					.orWhere("id = ?")
					.getSql();
		
		String expected = "DELETE FROM table_name"
				+ " WHERE id > 1"
				+ " AND id < ?"
				+ " OR (id = ?)";
		
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}

}
