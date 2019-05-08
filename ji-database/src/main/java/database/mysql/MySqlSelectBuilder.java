package database.mysql;

import java.util.List;

import database.support.DatabaseRow;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.SelectQueryBuilder;
import querybuilder.join.Join;

public class MySqlSelectBuilder extends AbstractBuilder implements SelectQueryBuilder {

	public MySqlSelectBuilder(final DoubleConsumer consumer, final String select) {
		super(consumer);
	}

	@Override
	public SelectQueryBuilder from(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder join(String table, Join join, String on) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder where(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder and(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder or(String where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder orderBy(String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder groupBy(String groupBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder having(String having) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder limit(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder offset(int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SelectQueryBuilder addParameter(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fetchSingle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatabaseRow fetchRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DatabaseRow> fetchAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
