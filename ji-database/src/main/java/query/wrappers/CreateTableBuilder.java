package query.wrappers;

import query.executors.MultipleExecute;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public interface CreateTableBuilder extends MultipleExecute<CreateTableBuilder> {

	CreateTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	CreateTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate);

}
