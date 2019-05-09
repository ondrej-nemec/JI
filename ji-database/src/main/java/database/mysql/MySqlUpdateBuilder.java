package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	private final String query;
	
	public MySqlUpdateBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "UPDATE " + table;
	}

	private MySqlUpdateBuilder(final DoubleConsumer consumer, final String query, final String partQuery) {
		super(consumer);
		this.query = query + " " + partQuery;
	}

	@Override
	public UpdateQueryBuilder set(String update) {
		return new MySqlUpdateBuilder(this.consumer, query, "SET " + update);
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "WHERE " + where);
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "AND " + where);
	}

	@Override
	public UpdateQueryBuilder orWhere(String where) {
		return new MySqlUpdateBuilder(this.consumer, query, "OR (" + where + ")");
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, String value) {
		throw new NotImplementedYet();
	}

	@Override
	public void execute() {
		throw new NotImplementedYet();
	}

	@Override
	public String getSql() {
		return query;
	}

}
