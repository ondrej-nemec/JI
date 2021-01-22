package querybuilder.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

import database.support.DoubleConsumer;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer<?> mock = mock(DoubleConsumer.class);
		UpdateQueryBuilder builder = new MySqlUpdateBuilder(null, "table_name")
					.set("name = :name")
					.set("value = :value")
					.where("id = :id")
					.andWhere("name = :actualName")
					.orWhere("name = :actualName")
					.addParameter(":name", "Name")
					.addParameter(":value", "Value")
					.addParameter(":id", 1)
					.addParameter(":actualName", "AnotherName");
		
		String expected = "UPDATE table_name"
				+ " SET name = :name, value = :value"
				+ " WHERE (id = :id)"
				+ " AND (name = :actualName)"
				+ " OR (name = :actualName)";

		String sql = "UPDATE table_name"
				+ " SET name = 'Name', value = 'Value'"
				+ " WHERE (id = 1)"
				+ " AND (name = 'AnotherName')"
				+ " OR (name = 'AnotherName')";
		
		assertEquals(expected, builder.getSql());
		assertEquals(sql, builder.createSql());
		verifyNoMoreInteractions(mock);
	}

}
