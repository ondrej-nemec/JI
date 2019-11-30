package migration;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.mysql.MySqlQueryBuilder;

@RunWith(JUnitParamsRunner.class)
public class MigrationToolEndToEndTest {
	
	////////////////////
	// v db jsou migrace - vrati vsechna id
	// v db neni migrace, ale je tabulka - vrati prazdne
	// neni db - vrati prazdne + vytvory tabulku
	////////////
	// zacatek transakce
	// provedeni migrace
	// zapis do tabulky migraci
	// commit migrace
	
	// migrace provedena, zaznam v tabulce
	////////////////
	// zacatek transakce
	// vrzeni v migraci
	// rollback
	
	// test nezapsana migrace, nejsou zmeny
	/////////////////////////

	
	private final String dbName = "javainit_migration_test";
	
	@Test
	@Parameters(method = "dataMigrateMakeMigrations")
	public void testMigrateMakeMigrations(String folder) throws Exception {
		fail("Not finished");
		Connection c = createConnection();
		c.setAutoCommit(false);
		
		MySqlQueryBuilder queryBuilder = new MySqlQueryBuilder(c);
		MigrationTool tool = new MigrationTool(queryBuilder, folder, Mockito.mock(Logger.class));
		
		testStates(c, false);
		tool.migrate();
		testStates(c, true);
		tool.revert();
		testStates(c, false);
		
		c.rollback();
		c.close();
	}
	
	private void testStates(Connection c, boolean exists) throws SQLException {
		String[] tables = new String[] {"First_table", "Second", "first_to_second", "migrations"};
		for (String table : tables) {
			try {
				c.prepareStatement("select * from " + table).execute();
			} catch (SQLException e) {
				if (exists) {
					throw e;
				}
			}
		}
	}

	public Object[] dataMigrateMakeMigrations() {
		return new Object[] {
			new Object[] {
				"migration"
			},
			new Object[] {
				"test/migration"
			}
		};
	}
	
	@Test(expected = IOException.class)
	public void testMigrateThrowsIfNotExistingFolderGiven() throws Exception {
		try (Connection c = createConnection()) {
			MySqlQueryBuilder queryBuilder = new MySqlQueryBuilder(c);
			MigrationTool tool = new MigrationTool(queryBuilder, "not-existing-folder", Mockito.mock(Logger.class));
			tool.migrate();
		}
	}
	
	private Connection createConnection() throws SQLException {
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "");
		prop.setProperty("serverTimezone", "Europe/Prague");
		
		DriverManager
			.getConnection("jdbc:mysql://localhost:3306/", prop)
			.createStatement()
			.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, prop);
	}
	
}
