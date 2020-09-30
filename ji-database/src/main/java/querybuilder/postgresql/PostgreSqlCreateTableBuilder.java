package querybuilder.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.CreateTableQueryBuilder;
import querybuilder.OnAction;

public class PostgreSqlCreateTableBuilder implements CreateTableQueryBuilder {

	private final StringBuilder sql;
	
	private final StringBuilder append;
	
	private boolean first = true;
	
	private final Connection connection;
	
	public PostgreSqlCreateTableBuilder(Connection connection, String table) {
		this.connection = connection;
		this.sql = new StringBuilder("CREATE TABLE " + table + " (");
		this.append = new StringBuilder();
	}
	
	@Override
	public CreateTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public CreateTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		if (!first) {
			sql.append(",");
		}
		first = false;
		sql.append(" ").append(name).append(" ");
		sql.append(EnumToPostgresqlString.typeToString(type));
		sql.append(EnumToPostgresqlString.defaultValueToString(defaultValue));
		for (ColumnSetting setting : settings) {
			sql.append(EnumToPostgresqlString.settingToString(setting, name, append));
		}
		
		return this;
	}

	@Override
	public CreateTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		sql.append(String.format(", FOREIGN KEY (%s) REFERENCES %s(%s)", column, referedTable, referedColumn));
		return this;
	}

	@Override
	public CreateTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		sql.append(String.format(
				", CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE %s ON UPDATE %s",
				column, column,
				referedTable,
				referedColumn,
				EnumToPostgresqlString.onActionToString(onDelete),
				EnumToPostgresqlString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			stat.executeUpdate(getSql());
		}
	}

	@Override
	public String getSql() {
		sql.append(append.toString());
		sql.append(")");
		return sql.toString();
	}

}
