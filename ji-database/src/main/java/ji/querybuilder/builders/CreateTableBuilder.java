package ji.querybuilder.builders;

import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.OnAction;
import ji.querybuilder.executors.MultipleExecute;

public interface CreateTableBuilder extends MultipleExecute<CreateTableBuilder> {

	CreateTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	CreateTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	CreateTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate);

}
