package database.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Test;

import database.support.DoubleConsumer;

public class MySqlUpdateBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlUpdateBuilder(null, "table_name")
					.set("name = :name")
					.where("id = :id")
					.andWhere("name = :actualName1")
					.orWhere("name = :actualName2")
					.getSql();
		String expected = "UPDATE table_name"
				+ " SET name = :name"
				+ " WHERE id = :id"
				+ " AND name = :actualName1"
				+ " OR (name = :actualName2)";
		
		// TODO add params test
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}
	
	@Test
	public void testExecute() {
		fail();
	}

}
