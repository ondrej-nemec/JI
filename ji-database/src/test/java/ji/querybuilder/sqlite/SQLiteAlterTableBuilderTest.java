package ji.querybuilder.sqlite;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.OnAction;

public class SQLiteAlterTableBuilderTest {
	
	@Test
	public void testBuilderViaSql() {
		Connection con = mock(Connection.class);
		AlterTableBuilder builder = new SQLiteAlterTableBuilder(con, "Table1")
				.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL)
				.addColumn("Column2", ColumnType.integer(), 1)
				.deleteColumn("Column3")
				.addForeingKey("Column", "Table2", "id", OnAction.RESTRICT, OnAction.SET_DEFAULT)
				.deleteForeingKey("Column")
				.modifyColumnType("Column4", ColumnType.integer())
				.renameColumn("Column5", "Column6", ColumnType.integer());
		
		// TODO tohle je potreba upravit
		String expected = "ALTER TABLE Table1"
				+ " ADD Column1 INT NOT NULL,"
				+ " ADD Column2 INT DEFAULT 1,"
				+ " DROP COLUMN Column3,"
				+ " ADD CONSTRAINT FK_Column FOREIGN KEY (Column) REFERENCES Table2(id) ON DELETE RESTRICT ON UPDATE SET DEFAULT,"
				+ " DROP FOREIGN KEY FK_Column,"
				+ " MODIFY Column4 INT,"
				+ " CHANGE COLUMN Column5 Column6 INT";
		
		assertEquals(expected, builder.getSql());
		verifyNoMoreInteractions(con);
	}

}
