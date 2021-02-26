package querybuilder.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import querybuilder.AlterTableQueryBuilder;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.OnAction;

public class SqlServerAlterTableBuilder implements AlterTableQueryBuilder {
	
	private final Connection connection;
	
	private final List<String> separatedQueries = new LinkedList<>();
	private final String prefix;
	private final String tableName;

	public SqlServerAlterTableBuilder(Connection connection, String name) {
		this.connection = connection;
		this.prefix = "ALTER TABLE " + name;
		this.tableName = name;
	}
	
	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, ColumnSetting... settings) {
		addColumn(name, type, null, settings);
		return this;
	}

	@Override
	public AlterTableQueryBuilder addColumn(String name, ColumnType type, Object defaultValue, ColumnSetting... settings) {
		StringBuilder sql = new StringBuilder(prefix);
		sql.append(" ADD ").append(name).append(" ");
		sql.append(EnumToSqlServerString.typeToString(type));
		sql.append(EnumToSqlServerString.defaultValueToString(defaultValue));
		StringBuilder append = new StringBuilder();
		for (ColumnSetting setting : settings) {
			sql.append(EnumToSqlServerString.settingToString(setting, name, append));
		}
		sql.append(append.toString());
		separatedQueries.add(sql.toString());
		return this;
	}

	@Override
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		separatedQueries.add(getAddFkString(column, referedTable, referedColumn));
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
	public AlterTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn, OnAction onDelete, OnAction onUpdate) {
		separatedQueries.add(String.format(
		        getAddFkString(column, referedTable, referedColumn)
				+ " ON DELETE %s ON UPDATE %s",
				EnumToSqlServerString.onActionToString(onDelete),
				EnumToSqlServerString.onActionToString(onUpdate)
		));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteColumn(String name) {
	    separatedQueries.add(String.format(prefix + " DROP COLUMN %s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder deleteForeingKey(String name) {
	    separatedQueries.add(String.format(prefix + " DROP CONSTRAINT FK_%s", name));
		return this;
	}

	@Override
	public AlterTableQueryBuilder modifyColumnType(String name, ColumnType type) {
	    separatedQueries.add(String.format(prefix + " ALTER COLUMN %s %s", name, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public AlterTableQueryBuilder renameColumn(String originName, String newName, ColumnType type) {
		separatedQueries.add(String.format("EXEC sp_rename '%s.%s', '%s', 'COLUMN'", tableName, originName, newName, EnumToSqlServerString.typeToString(type)));
		return this;
	}

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
		    for (String batch : separatedQueries) {
		        stat.addBatch(batch);
		    }
		    stat.executeBatch();
		}
	}

	@Override
	public String getSql() {
	    StringBuilder b = new StringBuilder();
	    separatedQueries.forEach((q)->{
	        b.append(q).append(";");
	    });
		return b.toString();
	}

}
