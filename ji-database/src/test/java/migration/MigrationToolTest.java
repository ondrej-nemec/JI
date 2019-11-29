package migration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import common.Logger;
import common.MapInit;
import common.OperationSystem;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.CreateTableQueryTable;
import querybuilder.InsertQueryBuilder;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;

@RunWith(JUnitParamsRunner.class)
public class MigrationToolTest {
	
	private final String migrationTable = "migrations";
	
	@Test
	@Parameters(method = "dataGetMigrationDirLoadFromClasspathAndFromTreeDir")
	public void testGetMigrationDirLoadFromClasspathAndFromTreeDir(String folder, String expected) throws IOException {
		MigrationTool tool = new MigrationTool(
				Mockito.mock(QueryBuilder.class),
				folder,
				Mockito.mock(Logger.class)
		);
		File f = tool.getMigrationDir(folder);
		assertNotEquals(-1, f.toString().indexOf(expected));
	}
	
	public Object[] dataGetMigrationDirLoadFromClasspathAndFromTreeDir() {
		return new Object[] {
			new Object[] {
				"migration",
				String.format(
					"javainit%sji-database%sbin%smigration",
					OperationSystem.PATH_SEPARATOR,
					OperationSystem.PATH_SEPARATOR,
					OperationSystem.PATH_SEPARATOR
				)
			},
			new Object[] {
				String.format("test%smigration", OperationSystem.PATH_SEPARATOR),
				String.format("test%smigration", OperationSystem.PATH_SEPARATOR)
			}
		};
	}
	
	@Test
	@Parameters(method = "dataLoadFilesWorks")
	public void testLoadFilesWorks(String folder, File[] files, Map<String, String> expected) {
		MigrationTool tool = new MigrationTool(
				Mockito.mock(QueryBuilder.class),
				folder,
				Mockito.mock(Logger.class)
		);
		Map<String, String> prop = tool.loadFiles(files);
		assertEquals(expected, prop);
	}
	
	public Object[] dataLoadFilesWorks() {
		File[] files = new File[] {
			new File("ALLWAYS_1__allways.class"),
			new File("M_1__First.class"),
			new File("M_2__Second.sql")
		};
		Map<String, String> map = new TreeMap<>();
		map.put("ALLWAYS_1__allways", "class");
		map.put("M_1__First", "class");
		map.put("M_2__Second", "sql");
		
		return new Object[] {
			new Object[] {
				"/migration",
				files,
				map
			},
			new Object[] {
				String.format("test%smigration", OperationSystem.PATH_SEPARATOR),
				files,
				map
			}
		};
	}
	
	/************ MIGRATIONS ****************/
	
	@Test
	@Parameters(method = "dataMigratedIds")
	public void testMigratedIds(QueryBuilder builder, List<String> expected, Consumer<Void> verify) throws SQLException {
		MigrationTool tool = new MigrationTool(builder, "", Mockito.mock(Logger.class));
		assertEquals(expected, tool.migratedIds());
		verify.accept(null);
	}
	
	public Object[] dataMigratedIds() throws SQLException {
		return new Object[] {
			dataMigratedIds1(),
			dataMigratedIds2(),
			dataMigratedIds3()
		};
	}
	
	private Object[] dataMigratedIds1() throws SQLException {
		List<String> migrations = Arrays.asList("first__first", "second__second");
		
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(migrationTable)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(migrations);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		Consumer<Void> verify = (a)->{
			try {
				verify(select, times(1)).from(migrationTable);
				verify(select, times(1)).fetchAll(any());
			
				verify(builder, times(1)).select("id");
				
				verifyNoMoreInteractions(builder);
				verifyNoMoreInteractions(select);
			} catch (SQLException e) {
				fail("SQL Exception " + e.getMessage());
			}
		};
		return new Object[] {builder, migrations, verify};
	}
	
	private Object[] dataMigratedIds2() throws SQLException {
		List<String> migrations = Arrays.asList();
		
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(migrationTable)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(migrations);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		Consumer<Void> verify = (a)->{
			try {
				verify(select, times(1)).from(migrationTable);
				verify(select, times(1)).fetchAll(any());
			
				verify(builder, times(1)).select("id");
				
				verifyNoMoreInteractions(builder);
				verifyNoMoreInteractions(select);
			} catch (SQLException e) {
				e.printStackTrace();
				fail();
			}
		};
		return new Object[] {builder, migrations, verify};
	}
	
	private Object[] dataMigratedIds3() throws SQLException {		
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(migrationTable)).thenReturn(select);
		when(select.fetchAll(any())).thenThrow(SQLException.class);
		
		CreateTableQueryTable create = mock(CreateTableQueryTable.class);
		when(create.addColumn(any(), any(), any())).thenReturn(create);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		when(builder.createTable(migrationTable)).thenReturn(create);
		
		Consumer<Void> verify = (a)->{
			try {
				verify(select, times(1)).from(migrationTable);
				verify(select, times(1)).fetchAll(any());
				
				verify(create, times(3)).addColumn(any(), any(), any());
				verify(create, times(1)).execute();
			
				verify(builder, times(1)).select("id");
				verify(builder, times(1)).createTable(migrationTable);
				
				verifyNoMoreInteractions(create);
				verifyNoMoreInteractions(builder);
				verifyNoMoreInteractions(select);
			} catch (SQLException e) {
				e.printStackTrace();
				fail();
			}
		};
		return new Object[] {builder, Arrays.asList(), verify};
	}
	
	@Test
	public void testDoMigrationDoNotMigrateMigratedMigration() throws Exception {
		Connection con = mock(Connection.class);
		InsertQueryBuilder insert = mock(InsertQueryBuilder.class);
		QueryBuilder builder = mock(QueryBuilder.class);
		
		MigrationTool tool = new MigrationTool(builder, "", Mockito.mock(Logger.class));
		try {
			tool.doTransaction(
					con,
					"Working__Migration",
					Arrays.asList("Working__Migration"),
					MapInit.hashMap(MapInit.t("Working__Migration", "java")),
					false,
					"migration.transaction",
					getClass().getClassLoader()
				);
		} catch (SQLException ignored) {}
		
		verify(con).setAutoCommit(false);
		verify(con).commit();
		
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(con);
		verifyNoMoreInteractions(builder);
	}
	
	@Test
	public void testDoTransactionDoMigrationAndWriteToTable() throws Exception {
		Connection con = mock(Connection.class);
		
		InsertQueryBuilder insert = mock(InsertQueryBuilder.class);
		when(insert.addValue(anyString(), anyString())).thenReturn(insert);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.insert(migrationTable)).thenReturn(insert);
		
		MigrationTool tool = new MigrationTool(builder, "", Mockito.mock(Logger.class));
		try {
			tool.doTransaction(
					con,
					"Working__Migration",
					Arrays.asList("first"),
					MapInit.hashMap(MapInit.t("Working__Migration", "java")),
					false,
					"migration.transaction",
					getClass().getClassLoader()
				);
		} catch (SQLException ignored) {}
		
		verify(con).setAutoCommit(false);
		verify(con).commit();
		verify(builder).select("*");
		verify(builder).insert(migrationTable);
		
		verify(insert, times(3)).addValue(anyString(), anyString());
		verify(insert, times(1)).execute();
		
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(con);
		verifyNoMoreInteractions(builder);
	}
	
	@Test
	public void testDotransactionDoNotInsertInTableWithThrowingMigration() throws Exception {
		Connection con = mock(Connection.class);
		QueryBuilder builder = mock(QueryBuilder.class);
		
		MigrationTool tool = new MigrationTool(builder, "", Mockito.mock(Logger.class));
		try {
			tool.doTransaction(
					con,
					"Throwing__Migration",
					Arrays.asList("first"),
					MapInit.hashMap(MapInit.t("Throwing__Migration", "java")),
					false,
					"migration.transaction",
					getClass().getClassLoader()
				);
		} catch (SQLException ignored) {}
		
		verify(con).setAutoCommit(false);
		verify(con).rollback();
		
		verifyNoMoreInteractions(con);
		verifyNoMoreInteractions(builder);
	}
	
	@Test
	public void testDoTransactionDoNotWriteAllwaysMigrationToMigrationTable() throws Exception {
		Connection con = mock(Connection.class);
		
		InsertQueryBuilder insert = mock(InsertQueryBuilder.class);
		when(insert.addValue(anyString(), anyString())).thenReturn(insert);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.insert(migrationTable)).thenReturn(insert);
		
		MigrationTool tool = new MigrationTool(builder, "", Mockito.mock(Logger.class));
		try {
			tool.doTransaction(
					con,
					"ALLWAYS_1__Migration",
					Arrays.asList("first"),
					MapInit.hashMap(MapInit.t("ALLWAYS_1__Migration", "java")),
					false,
					"migration.transaction",
					getClass().getClassLoader()
				);
		} catch (SQLException ignored) {}
		
		verify(con).setAutoCommit(false);
		verify(con).commit();
		verify(builder).createView("viewName");
		
		verifyNoMoreInteractions(insert);
		verifyNoMoreInteractions(con);
		verifyNoMoreInteractions(builder);
	}

	@Test
	public void testDoMigration() throws Exception {
		Statement stat = mock(Statement.class);
		Connection con = mock(Connection.class);
		when(con.createStatement()).thenReturn(stat);
		
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(migrationTable)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(Arrays.asList());
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select(anyString())).thenReturn(select);
		when(builder.getConnection()).thenReturn(con);
		
		MigrationTool tool = new MigrationTool(builder, "migration/doMigrations", Mockito.mock(Logger.class));
		
		tool.doMigrations(
				new File("migration/doMigrations"),
				MapInit.hashMap(MapInit.t("V1__first", "java"), MapInit.t("V2__second", "sql")),
				"migration/doMigrations",
				false
		);
		verify(stat, times(1)).addBatch(anyString());
		verify(stat, times(1)).executeBatch();
		verify(con, times(2)).setAutoCommit(false);
		verify(con, times(2)).commit();
		verify(builder, times(1)).select("a");
		verifyNoMoreInteractions(stat);
		verifyNoMoreInteractions(con);
		verifyNoMoreInteractions(select);
		verifyNoMoreInteractions(builder);
	}
	
	@Test
	@Parameters(method = "dataLoadContentWorksWithDirTreeAndClasspath")
	public void testLoadContentWorksWithDirTreeAndClasspath(String file, String content, boolean external) throws IOException {
		MigrationTool tool = new MigrationTool(mock(QueryBuilder.class), "", Mockito.mock(Logger.class));
		assertEquals(content, tool.loadContent(file, false, external));
	}
	
	public Object[] dataLoadContentWorksWithDirTreeAndClasspath() {
		return new Object[] {
			new Object[] {
				"migration/content/classpath.sql", "classpath", false
			},
			new Object[] {
				"test/migration/dir-tree.sql", "dir-tree", true
			}
		};
	}
	
}
