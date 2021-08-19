package querybuilder.sqlserver;

import java.sql.Connection;

import query.buildersparent.MultyBuilderParent;
import query.wrappers.AlterTableBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class SqlServerAlterTableBuilder extends MultyBuilderParent implements AlterTableBuilder {

	private final String prefix;
	private final String tableName;
	
	public SqlServerAlterTableBuilder(Connection connection, String name) {
		super(connection);
		this.prefix = "ALTER TABLE " + name;
		this.tableName = name;
	}
	
	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		StringBuilder sql = new StringBuilder(prefix);
		sql.append(" ADD ").append(name).append(" ");
		sql.append(EnumToSqlServerString.typeToString(type));
		sql.append(EnumToSqlServerString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			sql.append(EnumToSqlServerString.settingToString(setting, name, append));
		}
		sql.append(append.toString());
		addBuilder(sql.toString());
		return this;
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		addBuilder(getAddFkString(column, referedTable, referedColumn));
		return this;
	}
	
	private String getAddFkString(String column, String referedTable, String referedColumn) {
	    return String.format(
		        prefix
		        + " ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s)",
		        column, column, referedTable, referedColumn
		);
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		addBuilder(String.format(
		        getAddFkString(column, referedTable, referedColumn)
				+ " ON DELETE %s ON UPDATE %s",
				EnumToSqlServerString.onActionToString(onDelete),
				EnumToSqlServerString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableBuilder deleteColumn(String name) {
		addBuilder(String.format(prefix + " DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableBuilder deleteForeingKey(String name) {
		addBuilder(String.format(prefix + " DROP CONSTRAINT FK_%s", name)); // FOREIGN KEY
		return this;
	}

	@Override
	public AlterTableBuilder modifyColumnType(String name, ColumnType type) {
		addBuilder(String.format(prefix + " ALTER COLUMN %s %s", name, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableBuilder renameColumn(String originName, String newName, ColumnType type) {
		addBuilder(String.format("EXEC sp_rename '%s.%s', '%s', 'COLUMN'", tableName, originName, newName, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

}
