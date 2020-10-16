package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
/*
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;*/
import org.junit.runners.Parameterized.Parameters;

import common.Logger;
import core.text.Text;
import core.text.basic.ReadText;
import database.Database;
import database.DatabaseConfig;
import database.support.DatabaseRow;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;
import querybuilder.mysql.MySqlQueryBuilder;

//@RunWith(Parameterized.class)
public class EndToEndTest {
	
	private final String type;
	
	private final String pathToDb;
	
	private final boolean isExternalServer;
	
	private final String username;
	
	private final String password;

	public EndToEndTest(String type, String pathToDb, String username, String password, boolean isExternalServer) {
		this.type = type;
		this.pathToDb = pathToDb;
		this.username = username;
		this.password = password;
		this.isExternalServer = isExternalServer;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] {
				"mysql",
				"//localhost",
				"root",
				"",
				true
		});
		result.add(new Object[] {
				"derby",
				"C:/software/DerbyDB/bin",
				"root",
				"",
				false
		});
		result.add(new Object[] {
				"postgresql",
				"C:/software/DerbyDB/bin",
				"postgres",
				"1234",
				true
		});
		return result;
	}
	
	/***************************************/

	private Database database;

	//@Before
	public void before() throws SQLException, IOException {		
		DatabaseConfig config = new DatabaseConfig(
				type,
				pathToDb,
				isExternalServer,
				"javainit_database_test",
				username,
				password,
				Arrays.asList("migrations"),
				"Europe/Prague",
				5
		);
		
		Logger logger = mock(Logger.class);
		this.database = new Database(config, logger);
		if (!isExternalServer) {
			database.startServer();
		}
		database.createDbIfNotExists();
		loadMigrations(config);
	}
	
	private void loadMigrations(DatabaseConfig config) throws SQLException, IOException {
		String[] files = new String[] {"V1__update", "V2__insert", "V3__delete", "V4__select"};
		for (String file : files) {
			String migration = "/migrations/" + config.type + "/" + file + ".sql";
			String sql = Text.read((br)->{
				return ReadText.asString(br);
			}, getClass().getResourceAsStream(migration));
			database.applyQuery((conn)->{
				Statement stat = conn.createStatement();
				String[] batches = sql.split(";");
				for (String batch : batches) {
					stat.addBatch(batch);
				}
				stat.executeBatch();
			});
		}
	}
	
	//@After
	public void after() throws SQLException {
		try {
			database.applyQuery((conn)->{
				Statement stat = conn.createStatement();
				String[] tables = new String [] {
						"update_table",
						"delete_table",
						"insert_table",
						"select_table",
						"joined_table",
				};
				for (String table : tables) {
					stat.executeUpdate("DROP TABLE " + table);
				}
			});
		} finally {
			if (!isExternalServer) {
    			database.stopServer();
    		}
		}
	}
	
	//@Test
	public void testQueryBuilderInstance() throws SQLException {
		database.applyBuilder((builder) -> {
			assertTrue(builder instanceof MySqlQueryBuilder);
		});
	}	
	
	//@Test
	public void testExecuteUpdate() throws SQLException {
		database.applyBuilder((builder) -> {
			int code = builder.update("update_table")
				   .set("name=%set")
				   .where("id > %id")
				   .andWhere("name=%whereName")
				   .orWhere("name=%orName")
				   .addParameter("%set", "setted name")
				   .addParameter("%id", 1)
				   .addParameter("%whereName", "set it")
				   .addParameter("%orName", "this too")
				   .execute();
			assertEquals(code, 2);
		});
		
		database.applyQuery((conn)->{
			ResultSet res = conn.createStatement().executeQuery("SELECT * FROM update_table");
			
			int id = 1;
			while(res.next()) {
				switch(id) {
					case 1: assertRow(res, id, "set it");break;
					case 2: assertRow(res, id, "setted name");break;
					case 3: assertRow(res, id, "setted name");break;
					case 4: assertRow(res, id, "this not");break;
					default: throw new RuntimeException("Upgrade your tests!");
				}
				id++;
			}
		});
	}
	
	private void assertRow(ResultSet res, int id, String name) throws SQLException {
		assertEquals(id, res.getInt("id"));
		assertEquals(name, res.getString("name"));
	}

	//@Test
	public void testExecuteDelete() throws SQLException {
		try {
		
		database.applyBuilder((builder) -> {
			int code = builder.delete("delete_table")
			   .where("id > %id")
			   .andWhere("name=%whereName")
			   .orWhere("name=%orWhere")
			   .addParameter("%id", 1)
			   .addParameter("%whereName", "delete this")
			   .addParameter("%orWhere", "this too")
			   .execute();
			assertEquals(code, 2);
		});
		
		database.applyQuery((conn)->{
			ResultSet res = conn.createStatement().executeQuery("SELECT * FROM delete_table");
			
			int id = 1;
			while(res.next()) {
				switch(id) {
					case 1: assertRow(res, id, "delete this");break;
					case 2: assertRow(res, 4, "this not");break;
					default: throw new RuntimeException("Upgrade your tests!");
				}
				id++;
			}
		});
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testExecuteInsert() throws SQLException {
		database.applyBuilder((builder) -> {
			int code = builder.insert("insert_table")
				.addValue("id", "1")
				.addValue("name", "column_name")
			    .execute();
			assertEquals(code, 1);
		});
		
		database.applyQuery((conn)->{
			ResultSet res = conn.createStatement().executeQuery("SELECT * FROM insert_table");
			
			int id = 1;
			while(res.next()) {
				switch(id) {
					case 1: assertRow(res, id, "column_name");break;
					default: throw new RuntimeException("Upgrade your tests!");
				}
				id++;
			}
		});
	}
	
	//@Test
	public void testExecuteSelect() throws SQLException {
		database.applyBuilder((builder) -> {
			SelectQueryBuilder res = builder.select("a.id a_id, b.id b_id, a.name a_name, b.name b_name")
			   .from("select_table a")
			   .join("joined_table b", Join.INNER_JOIN, "a.id = b.a_id")
			   .where("a.id > %id")
			   .andWhere("a.name = %a_name")
			   .orWhere("b.name = %b_name")
			   .groupBy("a.id")
			   .having("a.id < %a_id")
			   .orderBy("a.id ASC")
			   .limit(2, 0)
			   .addParameter("%id", 1)
			   .addParameter("%a_name", "name_a")
			   .addParameter("%b_name", "name_b")
			   .addParameter("%a_id", 6);
		
    		String expectedSingle = "2";
    		DatabaseRow expectedRow = new DatabaseRow();
    		expectedRow.addValue("a_id", "2");
    		expectedRow.addValue("b_id", "3");
    		expectedRow.addValue("a_name", "name 2");
    		expectedRow.addValue("b_name", "name_b");
    		
    
    		DatabaseRow expectedRow2 = new DatabaseRow();
    		expectedRow2.addValue("a_id", "4");
    		expectedRow2.addValue("b_id", "5");
    		expectedRow2.addValue("a_name", "name_a");
    		expectedRow2.addValue("b_name", "name 5");
    		
    		assertEquals(expectedSingle, res.fetchSingle());
    		assertEquals(expectedRow, res.fetchRow());
    		assertEquals(
    				Arrays.asList(expectedRow, expectedRow2),
    				res.fetchAll()
    		);
		});		
	}
	
}
