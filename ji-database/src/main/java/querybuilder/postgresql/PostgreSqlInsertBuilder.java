package querybuilder.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.InsertQueryBuilder;
import querybuilder.SQL;

public class PostgreSqlInsertBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final Map<String, String> params;
	
	private final Connection connection;
	
	public PostgreSqlInsertBuilder(final Connection connection, final String table) {
		this.connection = connection;
		this.query = "INSERT INTO " + table;
		this.params = new HashMap<>();
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, String value) {
		params.put(columnName, String.format("'%s'", SQL.escape(value)));
		return this;
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, boolean value) {
		params.put(columnName, Boolean.toString(value));
		return this;
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, int value) {
		params.put(columnName, Integer.toString(value));
		return this;
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, double value) {
		params.put(columnName, Double.toString(value));
		return this;
	}

	@Override
	public int execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			return stat.executeUpdate(getSql());
		}
	}

	@Override
	public String getSql() {
		String columns = "(";
		String values = "VALUES (";
		
		boolean first = true;
		for (String name : params.keySet()) {
			if (first) {
				first = false;
			} else {
				columns += ", ";
				values += ", ";
			}
			columns += name;
			values += params.get(name);
		}
		
		columns += ")";
		values += ")";
		return query + " " + columns + " " + values;
	}

}
