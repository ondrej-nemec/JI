package ji.querybuilder.mysql;

import java.sql.Connection;

import ji.querybuilder.builders.SelectBuilder;

public class MySqlSelectBuilder extends SelectWrapper<SelectBuilder> implements SelectBuilder {
	
	public MySqlSelectBuilder(Connection connection, String... select) {
		super(connection);
		select(select);
	}

	@Override
	public SelectBuilder _addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

	@Override
	protected SelectBuilder get() {
		return this;
	}
	
	/*
	protected MySqlSelectBuilder(String query, Connection connection) {
		super(connection);
		this.query.append(query);
	}
	
	protected static String createSelect(String... select) {
		return "SELECT " + Implode.implode(", ", select);
	}

	@Override
	public SelectBuilder from(String table) {
		query.append(" FROM " + table);
		return this;
	}

	@Override
	public SelectBuilder join(String table, Join join, String on) {
		query.append(" " + EnumToMysqlString.joinToString(join) +" " + table + " ON " + on);
		return this;
	}

	@Override
	public SelectBuilder where(String where) {
		query.append(" WHERE (" + where + ")");
		return this;
	}

	@Override
	public SelectBuilder andWhere(String where) {
		query.append(" AND (" + where + ")");
		return this;
	}

	@Override
	public SelectBuilder orWhere(String where) {
		query.append(" OR (" + where + ")");
		return this;
	}

	@Override
	public SelectBuilder orderBy(String orderBy) {
		query.append(" ORDER BY " + orderBy);
		return this;
	}

	@Override
	public SelectBuilder groupBy(String groupBy) {
		query.append(" GROUP BY " + groupBy);
		return this;
	}

	@Override
	public SelectBuilder having(String having) {
		query.append(" HAVING " + having);
		return this;
	}

	@Override
	public SelectBuilder limit(int limit, int offset) {
		query.append(" LIMIT " + limit + " OFFSET " + offset);
		return this;
	}

	@Override
	public SelectBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}
	*/
}
