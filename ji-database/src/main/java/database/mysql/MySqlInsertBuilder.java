package database.mysql;

import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
	}

	@Override
	public InsertQueryBuilder addColumns(String... columns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsertQueryBuilder values(String... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InsertQueryBuilder addParameter(String name, String value) {
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
