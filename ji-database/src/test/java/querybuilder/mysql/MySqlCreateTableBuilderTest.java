package querybuilder.mysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.Test;

import query.wrappers.CreateTableBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class MySqlCreateTableBuilderTest {
	
	@Test
	public void testBuilderViaSql() {
		Connection con = mock(Connection.class);
		CreateTableBuilder builder = new MySqlCreateTableBuilder(con, "Table1")
					.addColumn("Column_1", ColumnType.integer(), ColumnSetting.AUTO_INCREMENT, ColumnSetting.PRIMARY_KEY)
					.addColumn("Column_2", ColumnType.bool(), ColumnSetting.NOT_NULL)
					.addColumn("Column_3", ColumnType.datetime(), ColumnSetting.UNIQUE)
					.addColumn("Column_4", ColumnType.text(), ColumnSetting.NULL)
					.addColumn("Column_5", ColumnType.string(50), "some text", ColumnSetting.NOT_NULL)
					.addForeingKey("Column_4", "Table2", "id", OnAction.CASCADE, OnAction.SET_NULL);
		
		String expected = "CREATE TABLE Table1 ("
				+ " Column_1 INT AUTO INCREMENT,"
				+ " Column_2 BOOLEAN NOT NULL,"
				+ " Column_3 DATETIME UNIQUE,"
				+ " Column_4 TEXT,"
				+ " Column_5 VARCHAR(50) DEFAULT 'some text' NOT NULL,"
				+ " CONSTRAINT FK_Column_4 FOREIGN KEY (Column_4) REFERENCES Table2(id) ON DELETE CASCADE ON UPDATE SET NULL,"
				+ " PRIMARY KEY (Column_1)"
				+ ")";
		
		assertEquals(expected, builder.getSql());
		verifyNoMoreInteractions(con);
	}

}
