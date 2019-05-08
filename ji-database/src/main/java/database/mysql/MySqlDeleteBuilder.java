package database.mysql;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.DeleteQueryBuilder;

public class MySqlDeleteBuilder extends AbstractBuilder implements DeleteQueryBuilder {

	public MySqlDeleteBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public DeleteQueryBuilder where(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteQueryBuilder andWhere(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteQueryBuilder addParameter(String name, String value) {
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
