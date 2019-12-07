package querybuilder.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;


public class MySqlExecuteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		Connection mock = mock(Connection.class);
		MySqlExecuteBuilder builder = new MySqlExecuteBuilder(mock, "some given SQL");
		assertEquals("some given SQL", builder.getSql());
		verifyNoMoreInteractions(mock);
	}
}
