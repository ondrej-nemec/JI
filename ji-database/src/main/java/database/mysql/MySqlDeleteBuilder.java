package database.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilder extends AbstractBuilder implements DeleteQueryBuilder {
	
	private final String query;
	
	private final Map<String, String> params;
	
	private int returned;
	
	public MySqlDeleteBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "DELETE FROM " + table;
		this.params = new HashMap<>();
	}
	
	private MySqlDeleteBuilder(
			final DoubleConsumer consumer,
			final String query, 
			final String queryPart,
			final Map<String, String> params) {
		super(consumer);
		this.query = query + " " + queryPart;
		this.params = params;
	}

	@Override
	public DeleteQueryBuilder where(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "WHERE " + where, params);
	}

	@Override
	public DeleteQueryBuilder andWhere(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "AND " + where, params);
	}

	@Override
	public DeleteQueryBuilder orWhere(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "OR (" + where + ")", params);
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
		params.put(name, String.format("'%s'", value));
		return this;
	}

	@Override
	public int execute() throws SQLException {
		this.consumer.accept((conn)->{
			Statement stat = conn.createStatement();
			returned = stat.executeUpdate(createSql());
			stat.close();
		});
		return returned;
	}

	@Override
	public String getSql() {
		return query;
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
