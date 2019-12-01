package migration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import querybuilder.QueryBuilder;

public class SingleMigrationToolTest {

	// foward
		// prazdny list, vytvoreni databaze
		// prazdny list, bez vytvoreni databaze
		// list se vsemi soubory
		// list s casti migraci
	// backward
		// zadne migrace, prazdna tabulka
		// zadne migrace, chybi tabulka
		// vse odmigrovano
		// cast souboru neodmigrovala
	// pro dany soubor zacne transakci, vybere spravny zpusob migrace a transakci spravne ukonci
		// opacne - transakce, zpusob odmigrace, smazani zaznamu, ukonceni transakce
		// foward / revert dulezite jen pro insert/delete
		/*
		try {
			con.setAutoCommit(false);
			String type = files.get(key.toString()).toString();
	    	String name = key.toString();
	    	switch (type) {
	    		case "sql": migrate(path + "/" + name + ".sql", builder, revert, external); break;
	    		case "class":
	    		case "java": migrate(name, loader, external ? "" : path + ".", revert); break;
	    		default: break;
	    	}
	    	String[] names = parseName(name);
	    	if (!names[0].contains(ALLWAYS_ID)) {
	    		builder
	    		.insert(migrationTable)
	    		.addValue("id", names[0])
	    		.addValue("Description", names[1])
	    		.addValue("DateTime", DateTime.format("YYYY-mm-dd H:m:s"))
	    		.execute();
	    	}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}
		*/
	
	@Test
	public void testTransactionWithWorkingMigration() throws SQLException {
		Connection con = mock(Connection.class);
		
		QueryBuilder builder = mock(QueryBuilder.class);
		when(builder.getConnection()).thenReturn(con);
		
		verify(con, times(1)).setAutoCommit(false);
		verify(con, times(1)).commit();
		verifyNoMoreInteractions(con);
		fail("not finished");
	}
	
	@Test
	public void testTransactionWithThrowingMigration() {
		fail("not finished");
	}
}
