package querybuilder.derby;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;


public class DerbyExecuteBuilderTest {
	
	@Test
	public void testBuilderViaGetSql() {
		Connection mock = mock(Connection.class);
		DerbyExecuteBuilder builder = new DerbyExecuteBuilder(mock, "some given SQL");
		assertEquals("some given SQL", builder.getSql());
		verifyNoMoreInteractions(mock);
	}
}
