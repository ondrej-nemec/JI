package database.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilder extends AbstractBuilder implements DeleteQueryBuilder {
	
	private final String query;
	
	private final List<String> params;
	
	private int returned;
	
	public MySqlDeleteBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "DELETE FROM " + table;
		this.params = new LinkedList<>();
	}
	
	private MySqlDeleteBuilder(final DoubleConsumer consumer, final String query, final String queryPart, final List<String> params) {
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
	public DeleteQueryBuilder addParameter(String value) {
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
