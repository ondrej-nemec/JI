package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	public MySqlUpdateBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public UpdateQueryBuilder set(String update) {
		throw new NotImplementedYet();
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		throw new NotImplementedYet();
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		throw new NotImplementedYet();
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
		throw new NotImplementedYet();
	}

}
