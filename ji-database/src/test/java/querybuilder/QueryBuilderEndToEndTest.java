package querybuilder;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import database.support.DatabaseRow;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;

@RunWith(Parameterized.class)
public class QueryBuilderEndToEndTest {
	
	private final Function<Connection, QueryBuilder> createBuilder;
	
	private final Supplier<Connection> createConnection;
	
	private final Consumer<Void> startDbAndCreateSchema;
	
	private final Consumer<Void> stopDb;
	
	public QueryBuilderEndToEndTest(
			Function<Connection, QueryBuilder> createBuilder,
			Supplier<Connection> createConnection,
			Consumer<Void> startDbAndCreateSchema,
			Consumer<Void> stopDb) {
		this.createBuilder = createBuilder;
		this.createConnection = createConnection;
		this.startDbAndCreateSchema = startDbAndCreateSchema;
		this.stopDb = stopDb;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		List<Object[]> result = new LinkedList<>();
		result.add(new Object[] {
			
		});
		result.add(new Object[] {
			
		});
		return result;
	}
	
	/***************************************/

	private Connection connection;
	
	@Before
	public void before() throws SQLException, IOException {
		startDbAndCreateSchema.accept(null);
		this.connection = createConnection.get();
		
		// TODO load data
	}
	
	@After
	public void after() throws SQLException {
		//TODO delete migrations
		
		connection.close();
		stopDb.accept(null);
	}
/*	
	private void loadMigrations(DatabaseConfig config) throws SQLException, IOException {
		String[] files = new String[] {"V1__update", "V2__insert", "V3__delete", "V4__select"};
		for (String file : files) {
			String migration = "/migrations/" + config.type + "/" + file + ".sql";
			try (BufferedReader br = BufferedReaderFactory.buffer(getClass().getResourceAsStream(migration))) {
				StringBuilder sql = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sql.append(line);
					line = br.readLine();
				}
				database.applyQuery((conn)->{
					Statement stat = conn.createStatement();
					String[] batches = sql.toString().split(";");
					for (String batch : batches) {
						stat.addBatch(batch);
					}
					stat.executeBatch();
				});
			}
		}
	}
*/
	/*************************************/

	@Test
	public void testExecuteUpdate() throws SQLException {
		createBuilder.apply(connection)
			.update("update_table")
			.set("name=%set")
			.where("id > %id")
			.andWhere("name=%whereName")
			.orWhere("name=%orName")
			.addParameter("%set", "setted name")
			.addParameter("%id", 1)
			.addParameter("%whereName", "set it")
			.addParameter("%orName", "this too")
			.execute();
		
		ResultSet res = connection.createStatement().executeQuery("SELECT * FROM update_table");
		
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
	}
	
	private void assertRow(ResultSet res, int id, String name) throws SQLException {
		assertEquals(id, res.getInt("id"));
		assertEquals(name, res.getString("name"));
	}

	@Test
	public void testExecuteDelete() throws SQLException {
		int code = createBuilder.apply(connection)
			.delete("delete_table")
			.where("id > %id")
			.andWhere("name=%whereName")
			.orWhere("name=%orWhere")
			.addParameter("%id", 1)
			.addParameter("%whereName", "delete this")
			.addParameter("%orWhere", "this too")
			.execute();
		assertEquals(code, 2);
		
		ResultSet res = connection.createStatement().executeQuery("SELECT * FROM delete_table");
		
		int id = 1;
		while(res.next()) {
			switch(id) {
				case 1: assertRow(res, id, "delete this");break;
				case 2: assertRow(res, 4, "this not");break;
				default: throw new RuntimeException("Upgrade your tests!");
			}
			id++;
		}
	}
	
	@Test
	public void testExecuteInsert() throws SQLException {
		int code = createBuilder.apply(connection)
			.insert("insert_table")
			.addValue("id", "1")
			.addValue("name", "column_name")
			.execute();
		assertEquals(code, 1);
		
		ResultSet res = connection.createStatement().executeQuery("SELECT * FROM insert_table");
		
		int id = 1;
		while(res.next()) {
			switch(id) {
				case 1: assertRow(res, id, "column_name");break;
				default: throw new RuntimeException("Upgrade your tests!");
			}
			id++;
		}
	}
	
	@Test
	public void testExecuteSelect() throws SQLException {
		SelectQueryBuilder res = createBuilder.apply(connection)
			.select("a.id a_id, b.id b_id, a.name a_name, b.name b_name")
			.from("select_table a")
			.join("joined_table b", Join.INNER_JOIN, "a.id = b.a_id")
			.where("a.id > %id")
			.andWhere("a.name = %a_name")
			.orWhere("b.name = %b_name")
			.groupBy("a.id")
			.having("a.id < %a_id")
			.orderBy("a.id ASC")
			.limit(2)
			.offset(0)
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
		assertEquals(Arrays.asList(expectedRow, expectedRow2), res.fetchAll());	
	}
	
}
