package migration;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
			Arrays.asList("ji-database-a/migA", "ji-database-b/migB"), 
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
		when(con.createStatement()).thenReturn(mock(Statement.class));
		return con;
	}

}
