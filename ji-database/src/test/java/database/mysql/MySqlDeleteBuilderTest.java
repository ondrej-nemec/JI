package database.mysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import database.support.DoubleConsumer;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		DeleteQueryBuilder builder = new MySqlDeleteBuilder(mock, "table_name")
					.where("id > 1")
					.andWhere("id < %id")
					.orWhere("id = %id")
					.addParameter("%id", "12");
		
		String expected = "DELETE FROM table_name"
				+ " WHERE id > 1"
				+ " AND id < %id"
				+ " OR (id = %id)";
		
		String sql = "DELETE FROM table_name"
				+ " WHERE id > 1"
				+ " AND id < '12'"
				+ " OR (id = '12')";
		
		assertEquals(expected, builder.getSql());
		assertEquals(sql, builder.createSql());
		verifyNoMoreInteractions(mock);
	}

}
