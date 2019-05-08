package database.mysql;

import java.util.List;

import common.exceptions.NotImplementedYet;
import database.support.DatabaseRow;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;

public class MySqlSelectBuilder extends AbstractBuilder implements SelectQueryBuilder {

	public MySqlSelectBuilder(final DoubleConsumer consumer, final String select) {
		super(consumer);
	}

	@Override
	public SelectQueryBuilder from(String table) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder join(String table, Join join, String on) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder where(String where) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder and(String where) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder or(String where) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder orderBy(String orderBy) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder groupBy(String groupBy) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder having(String having) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder limit(int limit) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder offset(int offset) {
		throw new NotImplementedYet();
	}

	@Override
	public SelectQueryBuilder addParameter(String name, String value) {
		throw new NotImplementedYet();
	}

	@Override
	public String getSql() {
		throw new NotImplementedYet();
	}

	@Override
	public String fetchSingle() {
		throw new NotImplementedYet();
	}

	@Override
	public DatabaseRow fetchRow() {
		throw new NotImplementedYet();
	}

	@Override
	public List<DatabaseRow> fetchAll() {
		throw new NotImplementedYet();
	}
	
}
