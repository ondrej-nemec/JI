package querybuilder;

import java.sql.SQLException;

public interface CreateTableQueryBuilder {

	CreateTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	CreateTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	CreateTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	CreateTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate);
	
	void execute() throws SQLException;
	
	String getSql();
	
}
