package querybuilder.postgresql;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import querybuilder.builders.AlterTableBuilder;
import querybuilder.buildersparent.Builder;
import querybuilder.buildersparent.QueryBuilderParent;
import querybuilder.enums.ColumnSetting;
import querybuilder.enums.ColumnType;
import querybuilder.enums.OnAction;

public class PostgreSqlAlterTableBuilder extends QueryBuilderParent implements AlterTableBuilder {
	
	private boolean first = true;

	public PostgreSqlAlterTableBuilder(Connection connection, String name) {
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
		query.append(EnumToPostgresqlString.typeToString(type));
		query.append(EnumToPostgresqlString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			query.append(EnumToPostgresqlString.settingToString(setting, name, append));
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
				EnumToPostgresqlString.onActionToString(onDelete),
				EnumToPostgresqlString.onActionToString(onUpdate)
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
		query.append(String.format("DROP CONSTRAINT FK_%s", name));
		return this;
	}

	@Override
	public AlterTableBuilder modifyColumnType(String name, ColumnType type) {
		first();
		query.append(String.format("ALTER COLUMN %s TYPE %s", name, EnumToPostgresqlString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableBuilder renameColumn(String originName, String newName, ColumnType type) {
		first();
		query.append(String.format("RENAME COLUMN %s TO %s", originName, newName/*, EnumToPostgresqlString.typeToString(type)*/));
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
