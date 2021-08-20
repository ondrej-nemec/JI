package querybuilder.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.sql.Connection;

import org.junit.Test;

import querybuilder.builders.AlterTableBuilder;
import querybuilder.enums.ColumnSetting;
import querybuilder.enums.ColumnType;
import querybuilder.enums.OnAction;

public class SqlServerAlterTableBuilderTest {
	
	@Test
	public void testBuilderViaSql() {
		Connection con = mock(Connection.class);
		AlterTableBuilder builder = new SqlServerAlterTableBuilder(con, "Table1")
				.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL)
				.addColumn("Column2", ColumnType.integer(), 1)
				.deleteColumn("Column3")
				.addForeingKey("Column", "Table2", "id", OnAction.CASCADE, OnAction.SET_DEFAULT)
				.deleteForeingKey("Column")
				.modifyColumnType("Column4", ColumnType.integer())
				.renameColumn("Column5", "Column6", ColumnType.integer());
		
		String expected = "ALTER TABLE Table1"
				+ " ADD Column1 INT NOT NULL;"
				
				+ "ALTER TABLE Table1"
				+ " ADD Column2 INT DEFAULT 1;"
				
				+ "ALTER TABLE Table1"
				+ " DROP COLUMN Column3;"
				
				+ "ALTER TABLE Table1"
				+ " ADD CONSTRAINT FK_Column FOREIGN KEY (Column) REFERENCES Table2(id) ON DELETE CASCADE ON UPDATE SET DEFAULT;"

				+ "ALTER TABLE Table1"
				+ " DROP CONSTRAINT FK_Column;"

				+ "ALTER TABLE Table1"
				+ " ALTER COLUMN Column4 INT;"
				
				+ "EXEC sp_rename 'Table1.Column5', 'Column6', 'COLUMN';";
		
		assertEquals(expected, builder.getSql());
		verifyNoMoreInteractions(con);
	}

}
