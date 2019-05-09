package database.mysql;

import common.exceptions.NotImplementedYet;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.InsertQueryBuilder;

public class MySqlInsertBuilder extends AbstractBuilder implements InsertQueryBuilder {

	private final String query;
	
	public MySqlInsertBuilder(final DoubleConsumer consumer, final String table) {
		super(consumer);
		this.query = "INSERT INTO " + table;
	}

	private MySqlInsertBuilder(final DoubleConsumer consumer, final String query, final String partQuery) {
		super(consumer);
		this.query = query + " " + partQuery;
	}

	@Override
	public InsertQueryBuilder addColumns(String... columns) {
		return new MySqlInsertBuilder(this.consumer, query, getBrackedString(columns));
	}

	@Override
	public InsertQueryBuilder values(String... values) {		
		return new MySqlInsertBuilder(this.consumer, query, "VALUES " + getBrackedString(values));
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
		return query;
	}
	
	private String getBrackedString(final String... values) {
		String names = "(";
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				names += ", ";
			}
			names += values[i];
		}
		return names + ")";
	}
	
}
