package querybuilder.derby;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;

import querybuilder.builders.AlterTableBuilder;
import querybuilder.enums.ColumnSetting;
import querybuilder.enums.ColumnType;
import querybuilder.enums.OnAction;

public class DerbyAlterTableBuilderTest {
	
	@Test
	public void testBuilderViaSql() {
		Connection con = mock(Connection.class);
		AlterTableBuilder builder = new DerbyAlterTableBuilder(con, "Table1")
				.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL)
				.addColumn("Column2", ColumnType.integer(), 1)
				.deleteColumn("Column3")
				.addForeingKey("Column", "Table2", "id", OnAction.RESTRICT, OnAction.SET_DEFAULT)
				.deleteForeingKey("Column")
				.modifyColumnType("Column4", ColumnType.integer())
				.renameColumn("Column5", "Column6", ColumnType.integer());
		
		String expected = "ALTER TABLE Table1"
				+ " ADD Column1 INT NOT NULL,"
				+ " ADD Column2 INT DEFAULT 1,"
				+ " DROP COLUMN Column3,"
				+ " ADD CONSTRAINT FK_Column FOREIGN KEY (Column) REFERENCES Table2(id) ON DELETE RESTRICT ON UPDATE SET DEFAULT,"
				+ " DROP FOREIGN KEY FK_Column,"
				+ " ALTER COLUMN Column4 SET DATA TYPE INT;"
				+ " RENAME COLUMN Table1.Column5 TO Column6;";
		
		assertEquals(expected, builder.getSql());
		verifyNoMoreInteractions(con);
	}

}
