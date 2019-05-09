package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilder extends AbstractBuilder implements DeleteQueryBuilder {
	
	private final String query;

	public MySqlDeleteBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "DELETE " + table;
	}
	
	private MySqlDeleteBuilder(final DoubleConsumer consumer, final String query, final String queryPart) {
		super(consumer);
		this.query = query + " " + queryPart;
	}

	@Override
	public DeleteQueryBuilder where(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "WHERE " + where);
	}

	@Override
	public DeleteQueryBuilder andWhere(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "AND " + where);
	}

	@Override
	public DeleteQueryBuilder orWhere(String where) {
		return new MySqlDeleteBuilder(this.consumer, query, "OR (" + where + ")");
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, String value) {
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
