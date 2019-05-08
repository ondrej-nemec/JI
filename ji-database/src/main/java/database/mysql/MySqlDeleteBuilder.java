package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilder extends AbstractBuilder implements DeleteQueryBuilder {

	public MySqlDeleteBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public DeleteQueryBuilder where(String where) {
		throw new NotImplementedYet();
	}

	@Override
	public DeleteQueryBuilder andWhere(String where) {
		throw new NotImplementedYet();
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
		throw new NotImplementedYet();
	}

}
