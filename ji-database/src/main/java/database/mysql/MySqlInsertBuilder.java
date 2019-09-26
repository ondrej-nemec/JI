package database.mysql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final Map<String, String> params;
	
	private int returned;
	
	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "INSERT INTO " + table;
		this.params = new HashMap<>();
	}

	private MySqlInsertBuilder(
			final DoubleConsumer consumer,
			final String query,
			final String partQuery, 
			final Map<String, String> params) {
		super(consumer);
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
		this.consumer.accept((conn)->{
			Statement stat = conn.createStatement();
			returned = stat.executeUpdate(getSql());
			stat.close();
		});
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
