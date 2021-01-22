package querybuilder.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder implements UpdateQueryBuilder {

	private final String update;
	
	private final StringBuilder set;
	
	private final Map<String, String> params;
	
	private final Connection connection;
	
	public MySqlUpdateBuilder(final Connection connection, final String table) {
		this.connection = connection;
		this.update = "UPDATE " + table;
		this.set = new StringBuilder();
		this.params = new HashMap<>();
	}

	@Override
	public UpdateQueryBuilder set(String set) {
		if (this.set.toString().isEmpty()) {
			this.set.append(" SET " + set);
		} else {
			this.set.append(", " + set);
		}
		return this;
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		set.append(" WHERE (" + where + ")");
		return this;
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		set.append(" AND (" + where + ")");
		return this;
	}

	@Override
	public UpdateQueryBuilder orWhere(String where) {
		set.append( " OR (" + where + ")");
		return this;
	}

	@Override
	public UpdateQueryBuilder addNotEscapedParameter(String name, String value) {
		params.put(name, value);
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
		return update + set;
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
