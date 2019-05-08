package database.mysql;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.UpdateQueryBuilder;

public class MySqlUpdateBuilder extends AbstractBuilder implements UpdateQueryBuilder {

	public MySqlUpdateBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public UpdateQueryBuilder set(String update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateQueryBuilder where(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateQueryBuilder andWhere(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateQueryBuilder addParameter(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
