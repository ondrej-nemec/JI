package querybuilder.builders;

import querybuilder.enums.ColumnSetting;
import querybuilder.enums.ColumnType;
import querybuilder.enums.OnAction;
import querybuilder.executors.MultipleExecute;

public interface CreateTableBuilder extends MultipleExecute<CreateTableBuilder> {

	CreateTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	CreateTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate);

}
