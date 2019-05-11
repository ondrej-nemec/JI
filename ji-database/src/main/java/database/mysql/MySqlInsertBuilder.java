package database.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	private final String query;
	
	private final List<String> params;
	
	private int returned;
	
	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "INSERT INTO " + table;
		this.params = new LinkedList<>();
	}

	private MySqlInsertBuilder(final DoubleConsumer consumer, final String query, final String partQuery, final List<String> params) {
		super(consumer);
		this.query = query + " " + partQuery;
		this.params = params;
	}

	@Override
	public InsertQueryBuilder addColumns(String... columns) {
		return new MySqlInsertBuilder(this.consumer, query, getBrackedString(columns), params);
	}

	@Override
	public InsertQueryBuilder values(String... values) {		
		return new MySqlInsertBuilder(this.consumer, query, "VALUES " + getBrackedString(values), params);
	}

	@Override
	public InsertQueryBuilder addParameter(String value) {
		params.add(value);
		return this;
	}

	@Override
	public int execute() throws SQLException {
		this.consumer.accept((conn)->{
			PreparedStatement stat = conn.prepareStatement(query);
			for (int i = 0; i < params.size(); i++) {
				stat.setString(i+1, params.get(i));
			}			
			returned = stat.executeUpdate();
		});
		return returned;
	}

	@Override
	public String getSql() {
		return query;
	}
	
	private String getBrackedString(final String... values) {
		String names = "(";
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				names += ", ";
			}
			names += values[i];
		}
		return names + ")";
	}
	
}
