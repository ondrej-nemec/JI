package querybuilder.postgresql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;


public class PostgreSqlExecuteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		Connection mock = mock(Connection.class);
		PostgreSqlExecuteBuilder builder = new PostgreSqlExecuteBuilder(mock, "some given SQL");
		assertEquals("some given SQL", builder.getSql());
		verifyNoMoreInteractions(mock);
	}
}
