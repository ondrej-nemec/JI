package querybuilder.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	private final String update;
	
	private final String set;
	
	private final String where;
	
	private final Map<String, String> params;
	
	private int returned;
	
	public MySqlUpdateBuilder(final Connection connection, final String table) {
		super(connection);
		this.update = "UPDATE " + table;
		this.params = new HashMap<>();
		this.set = "";
		this.where = "";
	}

	private MySqlUpdateBuilder(
			final Connection connection,
			final String update,
			final String set,
			final String where,
			final Map<String, String> params) {
		super(connection);
		this.update = update;
		this.set = set;
		this.where = where;
		this.params = params;
	}

	@Override
	public UpdateQueryBuilder set(String set) {
		if (this.set.equals("")) {
			set = "SET " + set;
		} else {
			set = this.set + ", " + set;
		}
		return new MySqlUpdateBuilder(this.connection, update, set, where, params);
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		return new MySqlUpdateBuilder(this.connection, update, set, "WHERE " + where, params);
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		return new MySqlUpdateBuilder(this.connection, update, set, this.where + " AND " + where, params);
	}

	@Override
	public UpdateQueryBuilder orWhere(String where) {
		return new MySqlUpdateBuilder(this.connection, update, set, this.where + " OR (" + where + ")", params);
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, boolean value) {
		params.put(name, value ? "1" : "0");
		return this;
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, int value) {
		params.put(name, Integer.toString(value));
		return this;
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, double value) {
		params.put(name, Double.toString(value));
		return this;
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, String value) {
		params.put(name, String.format("'%s'", value));
		return this;
	}

	@Override
	public int execute() throws SQLException {
		Statement stat = connection.createStatement();
		returned = stat.executeUpdate(createSql());
		stat.close();
		return returned;
	}

	@Override
	public String getSql() {
		return update + " " + set + " " + where;
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
