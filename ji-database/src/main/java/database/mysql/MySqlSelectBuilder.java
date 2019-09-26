package database.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.support.DatabaseRow;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;

public class MySqlSelectBuilder extends AbstractBuilder implements SelectQueryBuilder {

	private final String query;
	
	private final Map<String, String> params;
	
	private String single;
	
	private DatabaseRow row;
	
	private List<DatabaseRow> rows;
	
	public MySqlSelectBuilder(final DoubleConsumer consumer, final String select) {
		super(consumer);
		this.query = "SELECT " + select;
		this.params = new HashMap<>();
	}
	
	private MySqlSelectBuilder(
			final DoubleConsumer consumer,
			final String query,
			final String partQuery,
			final Map<String, String> params) {
		super(consumer);
		this.query = query + " " + partQuery;
		this.params = params;
	}

	@Override
	public SelectQueryBuilder from(String table) {
		return new MySqlSelectBuilder(this.consumer, query, "FROM " + table, params);
	}

	@Override
	public SelectQueryBuilder join(String table, Join join, String on) {
		return new MySqlSelectBuilder(
				this.consumer,
				query,
				joinToString(join) +" " + table + " ON " + on,
				params
		);
	}

	@Override
	public SelectQueryBuilder where(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "WHERE " + where, params);
	}

	@Override
	public SelectQueryBuilder andWhere(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "AND " + where, params);
	}

	@Override
	public SelectQueryBuilder orWhere(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "OR (" + where + ")", params);
	}

	@Override
	public SelectQueryBuilder orderBy(String orderBy) {
		return new MySqlSelectBuilder(this.consumer, query, "ORDER BY " + orderBy, params);
	}

	@Override
	public SelectQueryBuilder groupBy(String groupBy) {
		return new MySqlSelectBuilder(this.consumer, query, "GROUP BY " + groupBy, params);
	}

	@Override
	public SelectQueryBuilder having(String having) {
		return new MySqlSelectBuilder(this.consumer, query, "HAVING " + having, params);
	}

	@Override
	public SelectQueryBuilder limit(int limit) {
		return new MySqlSelectBuilder(this.consumer, query, "LIMIT " + limit, params);
	}

	@Override
	public SelectQueryBuilder offset(int offset) {
		return new MySqlSelectBuilder(this.consumer, query, "OFFSET " + offset, params);
	}

	@Override
	public SelectQueryBuilder addParameter(String name, boolean value) {
		params.put(name, value ? "1" : "0");
		return this;
	}

	@Override
	public SelectQueryBuilder addParameter(String name, int value) {
		params.put(name, Integer.toString(value));
		return this;
	}

	@Override
	public SelectQueryBuilder addParameter(String name, double value) {
		params.put(name, Double.toString(value));
		return this;
	}

	@Override
	public SelectQueryBuilder addParameter(String name, String value) {
		params.put(name, String.format("'%s'", value));
		return this;
	}

	@Override
	public String getSql() {
		return query;
	}

	@Override
	public String createSql() {
		String query = getSql();
		for (String name : params.keySet()) {
			String value = params.get(name);
			query = query.replaceAll(name, value);
		}
		return query;
	}

	@Override
	public String fetchSingle() throws SQLException {
		this.consumer.accept((conn)->{
			Statement stat = conn.createStatement();
			ResultSet res  = stat.executeQuery(createSql());
			if (res.next()) {
				single = res.getString(1);
			}
			stat.close();
		});
		return single;
	}

	@Override
	public DatabaseRow fetchRow() throws SQLException {
		this.consumer.accept((conn)->{
			Statement stat = conn.createStatement();
			ResultSet res  = stat.executeQuery(createSql());
			row = new DatabaseRow();
			if (res.next()) {
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
					row.addValue(
						res.getMetaData().getColumnLabel(i),
						res.getString(i)
					);
				}
			}
			stat.close();
		});
		return row;
	}

	@Override
	public List<DatabaseRow> fetchAll() throws SQLException {
		this.consumer.accept((conn)->{
			Statement stat = conn.createStatement();
			ResultSet res  = stat.executeQuery(createSql());
			rows = new LinkedList<>();
			while (res.next()) {
				DatabaseRow row = new DatabaseRow();
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
					row.addValue(
						res.getMetaData().getColumnLabel(i),
						res.getString(i)
					);
				}
				rows.add(row);
			}
			stat.close();
		});
		return rows;
	}
	
	protected String joinToString(final Join join) {
		switch(join) {
			case FULL_OUTER_JOIN: throw new RuntimeException("Full Outer Join is not supported by mysql");
			case INNER_JOIN: return "JOIN";
			case LEFT_OUTER_JOIN: return "LEFT JOIN";
			case RIGHT_OUTER_JOIN: return "RIGHT JOIN";
			default: throw new RuntimeException("Not implemented join: " + join);
		}
	}
	
}
