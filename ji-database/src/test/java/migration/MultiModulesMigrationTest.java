package migration;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import common.Logger;
import querybuilder.QueryBuilder;
import querybuilder.mysql.MySqlQueryBuilder;

public class MultiModulesMigrationTest {
	
	@Test
	public void testMigrateWithDirs() throws Exception {
		QueryBuilder queryBuilder = new MySqlQueryBuilder(createCon());
		MigrationTool tool = new MigrationTool(
			Arrays.asList("test/migA", "test/migB"), 
			queryBuilder,
			Mockito.mock(Logger.class)
		);
		tool.migrate();
	}

	@Test
	public void testMigrateWithClassPath() throws Exception {
		QueryBuilder queryBuilder = new MySqlQueryBuilder(createCon());
		MigrationTool tool = new MigrationTool(
			Arrays.asList("migrationsA", "migrationsB"), 
			queryBuilder,
			Mockito.mock(Logger.class)
		);
		tool.migrate();
	}

	private Connection createCon() throws SQLException {
		Connection con = mock(Connection.class);
		when(con.prepareStatement(any())).thenReturn(mock(PreparedStatement.class));
		
		PreparedStatement statement = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);
		when(statement.getGeneratedKeys()).thenReturn(rs);
		when(con.prepareStatement(any(), anyInt())).thenReturn(statement);
		when(con.createStatement()).thenReturn(mock(Statement.class));
		return con;
	}

}
