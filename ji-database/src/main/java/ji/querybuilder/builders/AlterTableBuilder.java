package ji.querybuilder.builders;

import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.OnAction;
import ji.querybuilder.executors.MultipleExecute;

public interface AlterTableBuilder extends MultipleExecute<AlterTableBuilder> {

	AlterTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings);

	AlterTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings);
	
	AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn);
	
	AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate);
		
	AlterTableBuilder deleteColumn(String name);
	
	AlterTableBuilder deleteForeingKey(String name);
	
	AlterTableBuilder modifyColumnType(String name, ColumnType type);
	
	AlterTableBuilder renameColumn(String originName, String newName, ColumnType type);

}
