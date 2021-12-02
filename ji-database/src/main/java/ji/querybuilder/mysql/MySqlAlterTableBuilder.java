package ji.querybuilder.mysql;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.buildersparent.Builder;
import ji.querybuilder.buildersparent.QueryBuilderParent;
import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.OnAction;

public class MySqlAlterTableBuilder extends QueryBuilderParent implements AlterTableBuilder {
	
	private boolean first = true;

	public MySqlAlterTableBuilder(Connection connection, String name) {
		super(connection);
		query.append("ALTER TABLE " + name);
	}
	
	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		first();
		query.append("ADD ").append(name).append(" ");
		query.append(EnumToMysqlString.typeToString(type));
		query.append(EnumToMysqlString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			query.append(EnumToMysqlString.settingToString(setting, name, append));
		}
		query.append(append.toString());
		return this;
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		first();
		query.append(String.format("ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s)", column, column, referedTable, referedColumn));
		return this;
	}

	@Override
	public AlterTableBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		addForeingKey(column, referedTable, referedColumn);
		query.append(String.format(
				" ON DELETE %s ON UPDATE %s",
				EnumToMysqlString.onActionToString(onDelete),
				EnumToMysqlString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableBuilder deleteColumn(String name) {
		first();
		query.append(String.format("DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableBuilder deleteForeingKey(String name) {
		first();
		query.append(String.format("DROP FOREIGN KEY FK_%s", name));
		return this;
	}

	@Override
	public AlterTableBuilder modifyColumnType(String name, ColumnType type) {
		first();
		query.append(String.format("MODIFY %s %s", name, EnumToMysqlString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableBuilder renameColumn(String originName, String newName, ColumnType type) {
		first();
		query.append(String.format("CHANGE COLUMN %s %s %s", originName, newName, EnumToMysqlString.typeToString(type)));
		return this;
	}
	
	private void first() {
		if (!first) {
			query.append(", ");
		} else {
			query.append(" ");
		}
		first = false;
	}

	@Override
	public List<Builder> _getBuilders() {
		return Arrays.asList(this);
	}

	@Override
	public AlterTableBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

}
