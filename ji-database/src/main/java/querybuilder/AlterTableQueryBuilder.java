package querybuilder;

public interface AlterTableQueryBuilder {

	AlterTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	AlterTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	AlterTableQueryBuilder deleteColumn(String name);
	
	AlterTableQueryBuilder deleteForeingKey(String name);
	
	AlterTableQueryBuilder modifyColumnType(String name, ColumnType type);
	
	AlterTableQueryBuilder renameColumn(String originName, String newName, ColumnType type);
	
	void execute();
}
