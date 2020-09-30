package querybuilder.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.DeleteQueryBuilder;
import querybuilder.SQL;

public class PostgreSqlDeleteBuilder implements DeleteQueryBuilder {
	
	private final StringBuilder query;
	
	private final Map<String, String> params;
	
	private final Connection connection;
	
	public PostgreSqlDeleteBuilder(final Connection connection, final String table) {
		this.connection = connection;
		this.query = new StringBuilder("DELETE FROM " + table);
		this.params = new HashMap<>();
	}

	@Override
	public DeleteQueryBuilder where(String where) {
		query.append(" WHERE " + where);
		return this;
	}

	@Override
	public DeleteQueryBuilder andWhere(String where) {
		query.append(" AND " + where);
		return this;
	}

	@Override
	public DeleteQueryBuilder orWhere(String where) {
		query.append(" OR (" + where + ")");
		return this;
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, boolean value) {
		params.put(name, value ? "1" : "0");
		return this;
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, int value) {
		params.put(name, Integer.toString(value));
		return this;
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, double value) {
		params.put(name, Double.toString(value));
		return this;
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, String value) {
		params.put(name, String.format("'%s'", SQL.escape(value)));
		return this;
	}

	@Override
	public int execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			return stat.executeUpdate(createSql());
		}
	}

	@Override
	public String getSql() {
		return query.toString();
	}

	@Override
	public String createSql() {
		String query = getSql();
		for (String name : params.keySet()) {
			String value = params.get(name);
			query = query.replaceAll(name, value);
		}
		return query;
	}

}
