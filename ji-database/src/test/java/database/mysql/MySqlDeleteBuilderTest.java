package database.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;

import database.support.DoubleConsumer;

public class MySqlDeleteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer mock = mock(DoubleConsumer.class);
		String sql = new MySqlDeleteBuilder(mock, "table_name")
					.where("id > 1")
					.andWhere("id < :id")
					.orWhere("id = :c_id")
					.getSql();
		
		String expected = "DELETE table_name"
				+ " WHERE id > 1"
				+ " AND id < :id"
				+ " OR (id = :c_id)";
		
		// TODO add params test
		assertEquals(expected, sql);
		verifyNoMoreInteractions(mock);
	}
	
	@Test
	public void testExecute() {
		fail();
	}

}
