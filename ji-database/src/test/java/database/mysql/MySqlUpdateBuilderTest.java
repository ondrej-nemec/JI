package database.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

import database.support.DoubleConsumer;

public class MySqlUpdateBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlUpdateBuilder(null, "table_name")
					.set("name = :name")
					.set("value = :value")
					.where("id = :id")
					.andWhere("name = :actualName1")
					.orWhere("name = :actualName2")
					.getSql();
		String expected = "UPDATE table_name"
				+ " SET name = :name, value = :value"
				+ " WHERE id = :id"
				+ " AND name = :actualName1"
				+ " OR (name = :actualName2)";
		
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}

}
