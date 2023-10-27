package ji.querybuilder.derby;

import java.sql.Connection;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.buildersparent.MultyBuilderParent;
import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.OnAction;

public class DerbyAlterTableBuilder extends MultyBuilderParent implements AlterTableBuilder {
	
	private final String tableName;
	
	private boolean first = true;

	public DerbyAlterTableBuilder(Connection connection, String name) {
		super(connection);
		this.tableName = name;
	}
	
	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		first();
		mainBuilder.append("ADD ").append(name).append(" ");
		mainBuilder.append(EnumToDerbyString.typeToString(type));
		mainBuilder.append(EnumToDerbyString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			mainBuilder.append(EnumToDerbyString.settingToString(setting, name, append));
		}
		mainBuilder.append(append.toString());
		return this;
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		first();
		mainBuilder.append(String.format("ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s)", column, column, referedTable, referedColumn));
		return this;
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		addForeingKey(column, referedTable, referedColumn);
		mainBuilder.append(String.format(
				" ON DELETE %s ON UPDATE %s",
				EnumToDerbyString.onActionToString(onDelete),
				EnumToDerbyString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableBuilder deleteColumn(String name) {
		first();
		mainBuilder.append(String.format("DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableBuilder deleteForeingKey(String name) {
		first();
		mainBuilder.append(String.format("DROP FOREIGN KEY FK_%s", name));
		return this;
	}

	@Override
	public AlterTableBuilder modifyColumnType(String name, ColumnType type) {
		first();
		mainBuilder.append(String.format("ALTER COLUMN %s SET DATA TYPE %s", name, EnumToDerbyString.typeToString(type)));
		// ALTER TABLE bl.USERSPROPERTIES ALTER COLUMN Value SET DATA TYPE CHAR(32000)
		/*
		ALTER TABLE MY_TABLE ADD COLUMN NEW_COLUMN BLOB(2147483647);
		UPDATE MY_TABLE SET NEW_COLUMN=MY_COLUMN;
		ALTER TABLE MY_TABLE DROP COLUMN MY_COLUMN;
		RENAME COLUMN MY_TABLE.NEW_COLUMN TO MY_COLUMN;
		*/
		return this;
	}

	@Override
	public AlterTableBuilder renameColumn(String originName, String newName, ColumnType type) {
		// first();
		//sql.append(String.format("RENAME COLUMN %s %s %s", originName, newName, EnumToDerbyString.typeToString(type)));
		addBuilder(String.format(" RENAME COLUMN %s.%s TO %s", tableName, originName, newName));
		return this;
	}
	
	private void first() {
		if (!first) {
			mainBuilder.append(", ");
		} else {
			mainBuilder.append(String.format("ALTER TABLE %s " , tableName));
		}
		first = false;
	}

	@Override
	public AlterTableBuilder _addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

}
