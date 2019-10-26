package querybuilder.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final Map<String, String> params;
	
	private int returned;
	
	public MySqlInsertBuilder(final Connection connection, final String table) {
		super(connection);
		this.query = "INSERT INTO " + table;
		this.params = new HashMap<>();
	}

	private MySqlInsertBuilder(
			final Connection connection,
			final String query,
			final String partQuery, 
			final Map<String, String> params) {
		super(connection);
		this.query = query + " " + partQuery;
		this.params = params;
	}

	@Override
	public InsertQueryBuilder addValue(String columnName, String value) {
		params.put(columnName, String.format("'%s'", value));
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
		Statement stat = connection.createStatement();
		returned = stat.executeUpdate(getSql());
		stat.close();
		return returned;
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
