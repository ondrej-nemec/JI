package querybuilder.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import query.wrappers.CreateViewBuilder;
import querybuilder.Join;

@RunWith(JUnitParamsRunner.class)
public class SqlServerCreateViewBuilderTest {
	
	@Test
	@Parameters(method = "dataBuilderViaSql")
	public void testBuilderViaSql(boolean modify, String expected) {
		Connection con = mock(Connection.class);
		CreateViewBuilder builder = new SqlServerCreateViewBuilder(con, "View1", modify)
				.select("a.id ID", "b.id ID_2")
				.from("table_name a")
				.join("joined_table b", Join.INNER_JOIN, "a.id = b.id")
				.where("b.id = 1")
				.andWhere("b.name = %name")
				.addParameter("%name", "name")
				.orWhere("b.name = %nname")
				.addParameter("%nname", "nname")
				.groupBy("a.id")
				.orderBy("a.id DESC")
				.having("a.id > %id")
				.addParameter("%id", 10)
				.limit(0, 0);
		assertEquals(expected, builder.getSql());
		verifyNoMoreInteractions(con);
	}
	
	public Object[] dataBuilderViaSql() {
		String query = " VIEW View1 AS"
				+ " SELECT a.id ID, b.id ID_2"
				+ " FROM table_name a"
				+ " JOIN joined_table b ON a.id = b.id"
				+ " WHERE (b.id = 1)"
				+ " AND (b.name = %name)"
				+ " OR (b.name = %nname)"
				+ " GROUP BY a.id"
				+ " ORDER BY a.id DESC"
				+ " HAVING a.id > %id"
				+ " OFFSET 0 ROWS"
				+ " FETCH NEXT 0 ROWS ONLY;";
		return new Object[] {
			new Object[] {false, "CREATE" + query},
			new Object[] {true, "ALTER" + query}
		};
	}

}
