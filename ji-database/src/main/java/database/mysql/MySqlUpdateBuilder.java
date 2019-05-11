package database.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	private final String query;
	
	private final List<String> params;
	
	private int returned;
	
	public MySqlUpdateBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "UPDATE " + table;
		this.params = new LinkedList<>();
	}

	private MySqlUpdateBuilder(final DoubleConsumer consumer, final String query, final String partQuery, final List<String> params) {
		super(consumer);
		this.query = query + " " + partQuery;
		this.params = params;
	}

	@Override
	public UpdateQueryBuilder set(String update) {
		return new MySqlUpdateBuilder(this.consumer, query, "SET " + update, params);
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "WHERE " + where, params);
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "AND " + where, params);
	}

	@Override
	public UpdateQueryBuilder orWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "OR (" + where + ")", params);
	}

	@Override
	public UpdateQueryBuilder addParameter(String value) {
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

}
