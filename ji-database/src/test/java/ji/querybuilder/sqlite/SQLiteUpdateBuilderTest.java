package ji.querybuilder.sqlite;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

import ji.database.support.DoubleConsumer;
import ji.querybuilder.builders.UpdateBuilder;

public class SQLiteUpdateBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		DoubleConsumer<?> mock = mock(DoubleConsumer.class);
		UpdateBuilder builder = new SQLiteUpdateBuilder(null, "table_name")
					.set("name = :name")
					.set("value = :value")
					.where("id = :id")
					.andWhere("name = :actualName")
					.orWhere("name = :actualName")
					.addParameter(":name", "Name")
					.addParameter(":value", "Value")
					.addParameter(":id", 1)
					.addParameter(":actualName", "AnotherName");

		// TODO tohle je potreba upravit
		String expected = "UPDATE table_name"
				+ " SET name = :name, value = :value"
				+ " WHERE (id = :id)"
				+ " AND (name = :actualName)"
				+ " OR (name = :actualName)";

		// TODO tohle je potreba upravit
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
