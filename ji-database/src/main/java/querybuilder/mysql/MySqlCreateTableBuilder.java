package querybuilder.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.CreateTableQueryBuilder;

public class MySqlCreateTableBuilder implements CreateTableQueryBuilder {

	private final StringBuilder sql;
	
	private final StringBuilder append;
	
	private boolean first = true;
	
	private final Connection connection;
	
	public MySqlCreateTableBuilder(Connection connection, String table) {
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
		sql.append(typeToString(type));
		sql.append(defaultValueToString(defaultValue));
		for (ColumnSetting setting : settings) {
			sql.append(settingToString(setting, name));
		}
		
		return this;
	}
	
	private Object defaultValueToString(Object defaultValue) {
		if (defaultValue == null) {
			return "";
		}
		String value = (defaultValue instanceof String) ? String.format("'%s'", defaultValue) : defaultValue.toString();
		return " DEFAULT " + value;
	}

	private Object settingToString(ColumnSetting setting, String column) {
		switch (setting) {
    		case AUTO_INCREMENT: return " AUTO INCREMENT";
    		case UNIQUE: return " UNIQUE";
    		case NOT_NULL: return " NOT NULL";
    		case NULL: break;
    		case PRIMARY_KEY: append.append(String.format(", PRIMARY KEY (%s)", column)); break;
		}
		return "";
	}

	private String typeToString(ColumnType type) {
		switch (type.getType()) {
    		case STRING:
    			return String.format("VARCHAR(%s)", type.getSize());
    		default: return type.getType().toString();
		}
	}

	@Override
	public CreateTableQueryBuilder addForeingKey(String column, String referedTable, String referedColumn) {
		sql.append(String.format(", FOREIGN KEY (%s) REFERENCES %s(%s)", column, referedTable, referedColumn));
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
