package database.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	private final String update;
	
	private final String set;
	
	private final String where;
	
	private final List<String> params;
	
	private int returned;
	
	public MySqlUpdateBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.update = "UPDATE " + table;
		this.params = new LinkedList<>();
		this.set = "";
		this.where = "";
	}

	private MySqlUpdateBuilder(
			final DoubleConsumer consumer,
			final String update,
			final String set,
			final String where,
			final List<String> params) {
		super(consumer);
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
		return new MySqlUpdateBuilder(this.consumer, update, set, where, params);
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		return new MySqlUpdateBuilder(this.consumer, update, set, "WHERE " + where, params);
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, update, set, this.where + " AND " + where, params);
	}

	@Override
	public UpdateQueryBuilder orWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, update, set, this.where + " OR (" + where + ")", params);
	}

	@Override
	public UpdateQueryBuilder addParameter(String value) {
		params.add(value);
		return this;
	}

	@Override
	public int execute() throws SQLException {
		this.consumer.accept((conn)->{
			PreparedStatement stat = conn.prepareStatement(getSql());
			for (int i = 0; i < params.size(); i++) {
				stat.setString(i+1, params.get(i));
			}			
			returned = stat.executeUpdate();
		});
		return returned;
	}

	@Override
	public String getSql() {
		return update + " " + set + " " + where;
	}

}
