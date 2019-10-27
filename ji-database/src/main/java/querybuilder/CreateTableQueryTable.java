package querybuilder;

public interface CreateTableQueryTable {

	CreateTableQueryTable addColumn(String name, ColumnType type, ColumnSetting... settings);

	CreateTableQueryTable addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	CreateTableQueryTable addForeingKey(String column, String referedTable, String referedColumn);
	
	void execute();
	
}
