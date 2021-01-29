package querybuilder.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import querybuilder.AlterTableQueryBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class SqlServerAlterTableBuilder implements AlterTableQueryBuilder {
	
	private final Connection connection;
	
	private final StringBuilder sql;
	
	private boolean first = true;

	public SqlServerAlterTableBuilder(Connection connection, String name) {
		this.connection = connection;
		this.sql = new StringBuilder("ALTER TABLE " + name);
	}
	
	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		first();
		sql.append("ADD ").append(name).append(" ");
		sql.append(EnumToSqlServerString.typeToString(type));
		sql.append(EnumToSqlServerString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			sql.append(EnumToSqlServerString.settingToString(setting, name, append));
		}
		sql.append(append.toString());
		return this;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		first();
		sql.append(String.format("ADD CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s)", column, column, referedTable, referedColumn));
		return this;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		addForeingKey(column, referedTable, referedColumn);
		sql.append(String.format(
				" ON DELETE %s ON UPDATE %s",
				EnumToSqlServerString.onActionToString(onDelete),
				EnumToSqlServerString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteColumn(String name) {
		first();
		sql.append(String.format("DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteForeingKey(String name) {
		first();
		sql.append(String.format("DROP FOREIGN KEY FK_%s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder modifyColumnType(String name, ColumnType type) {
		first();
		sql.append(String.format("MODIFY %s %s", name, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableQueryBuilder renameColumn(String originName, String newName, ColumnType type) {
		first();
		sql.append(String.format("CHANGE COLUMN %s %s %s", originName, newName, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			stat.execute(getSql());
		}
	}

	@Override
	public String getSql() {
		return sql.toString();
	}
	
	private void first() {
		if (!first) {
			sql.append(", ");
		} else {
			sql.append(" ");
		}
		first = false;
	}

}
