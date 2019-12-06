package migration.resources;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.structures.Tuple2;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import migration.MigrationException;
import querybuilder.CreateTableQueryTable;
import querybuilder.QueryBuilder;
import querybuilder.SelectQueryBuilder;

@RunWith(JUnitParamsRunner.class)
public class MigrationPreparationTest {

	private static final String MIGRATION_TABLE = "migration";
	private static final String SEPARATOR = "__";

	@Test // (expected = MigrationException.class)
	public void testGetFilesToMigrateThrowIfMigrationGape() throws SQLException, MigrationException {
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(MIGRATION_TABLE)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(Arrays.asList("Second"));
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		MigrationPreparation prep = new MigrationPreparation(MIGRATION_TABLE, SEPARATOR);
		try {
			prep.getFilesToMigrate(Arrays.asList("First__migration.java", "Second__migration.sql"), false, builder);
			fail("Expected " + MigrationException.class);
		} catch (Exception e) {}
		
		verify(select, times(1)).from(MIGRATION_TABLE);
		verify(select, times(1)).fetchAll(any());
		verifyNoMoreInteractions(select);
		
		verify(builder, times(1)).select("id");
		verifyNoMoreInteractions(builder);
	}
	
	@Test
	@Parameters(method = "dataGetFilesToMigrate")
	public void testGetFilesToMigrate(
			List<String> result,
			List<String> loadedFiles,
			boolean isRevert,
			Tuple2<QueryBuilder, Callable<Void>> build) throws Exception {
		MigrationPreparation prep = new MigrationPreparation(MIGRATION_TABLE, SEPARATOR);
		assertEquals(result, prep.getFilesToMigrate(loadedFiles, isRevert, build._1()));
		build._2().call();
	}
	
	public Object[] dataGetFilesToMigrate( ) throws SQLException {
		List<String> loadedFiles = Arrays.asList("First__migration.java", "Second__migration.sql");
		return new Object[] {
			new Object[] {Arrays.asList("First__migration.java", "Second__migration.sql"), loadedFiles, false, builderCreateTable()},
			new Object[] {Arrays.asList("First__migration.java", "Second__migration.sql"), loadedFiles, false, builderNotCreate()},
			new Object[] {new LinkedList<>(), loadedFiles, false, builderAllMigrated()},
			new Object[] {Arrays.asList("Second__migration.sql"), loadedFiles, false, builderPartMigrated()},

			new Object[] {new LinkedList<>(), loadedFiles, true, builderCreateTable()},
			new Object[] {new LinkedList<>(), loadedFiles, true, builderNotCreate()},
			new Object[] {Arrays.asList("First__migration.java", "Second__migration.sql"), loadedFiles, true, builderAllMigrated()},
			new Object[] {Arrays.asList("First__migration.java"), loadedFiles, true, builderPartMigrated()},
		};
	}

	private Tuple2<QueryBuilder, Callable<Void>> builderPartMigrated() throws SQLException {
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(MIGRATION_TABLE)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(Arrays.asList("First"));
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		Callable<Void> validate = ()->{
			verify(select, times(1)).from(MIGRATION_TABLE);
			verify(select, times(1)).fetchAll(any());
			verifyNoMoreInteractions(select);
			
			verify(builder, times(1)).select("id");
			verifyNoMoreInteractions(builder);
			return null;
		};
		return new Tuple2<>(builder, validate);
	}

	private Tuple2<QueryBuilder, Callable<Void>> builderAllMigrated() throws SQLException {
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(MIGRATION_TABLE)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(Arrays.asList("First", "Second"));
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		Callable<Void> validate = ()->{
			verify(select, times(1)).from(MIGRATION_TABLE);
			verify(select, times(1)).fetchAll(any());
			verifyNoMoreInteractions(select);
			
			verify(builder, times(1)).select("id");
			verifyNoMoreInteractions(builder);
			return null;
		};
		return new Tuple2<>(builder, validate);
	}

	private Tuple2<QueryBuilder, Callable<Void>> builderCreateTable() throws SQLException {
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(MIGRATION_TABLE)).thenReturn(select);
		when(select.fetchAll(any())).thenThrow(SQLException.class);
		
		CreateTableQueryTable create = mock(CreateTableQueryTable.class);
		when(create.addColumn(any(), any(), any())).thenReturn(create);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		when(builder.createTable(MIGRATION_TABLE)).thenReturn(create);
		
		Callable<Void> validate = ()->{
			verify(select, times(1)).from(MIGRATION_TABLE);
			verify(select, times(1)).fetchAll(any());
			verifyNoMoreInteractions(select);
			
			verify(create, times(3)).addColumn(any(), any(), any());
			verify(create, times(1)).execute();
			verifyNoMoreInteractions(create);
			
			verify(builder, times(1)).select("id");
			verify(builder, times(1)).createTable(MIGRATION_TABLE);
			verifyNoMoreInteractions(builder);
			return null;
		};
		return new Tuple2<>(builder, validate);
	}

	private Tuple2<QueryBuilder, Callable<Void>> builderNotCreate() throws SQLException {
		SelectQueryBuilder select = mock(SelectQueryBuilder.class);
		when(select.from(MIGRATION_TABLE)).thenReturn(select);
		when(select.fetchAll(any())).thenReturn(new ArrayList<>());
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.select("id")).thenReturn(select);
		
		Callable<Void> validate = ()->{
			verify(select, times(1)).from(MIGRATION_TABLE);
			verify(select, times(1)).fetchAll(any());
			verifyNoMoreInteractions(select);
			
			verify(builder, times(1)).select("id");
			verifyNoMoreInteractions(builder);
			return null;
		};
		return new Tuple2<>(builder, validate);
	}

}
