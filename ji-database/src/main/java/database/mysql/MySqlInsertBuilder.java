package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public InsertQueryBuilder addColumns(String... columns) {
		throw new NotImplementedYet();
	}

	@Override
	public InsertQueryBuilder values(String... values) {
		throw new NotImplementedYet();
	}

	@Override
	public InsertQueryBuilder addParameter(String name, String value) {
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
