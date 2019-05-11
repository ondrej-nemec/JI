package database.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import common.exceptions.NotImplementedYet;
import database.support.DatabaseRow;
import database.support.DoubleConsumer;
import querybuilder.AbstractBuilder;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;

public class MySqlSelectBuilder extends AbstractBuilder implements SelectQueryBuilder {

	private final String query;
	
	private String single;
	
	private DatabaseRow row;
	
	private List<DatabaseRow> rows;
	
	public MySqlSelectBuilder(final DoubleConsumer consumer, final String select) {
		super(consumer);
		this.query = "SELECT " + select;
	}
	
	private MySqlSelectBuilder(final DoubleConsumer consumer, final String query, final String partQuery) {
		super(consumer);
		this.query = query + " " + partQuery;
	}

	@Override
	public SelectQueryBuilder from(String table) {
		return new MySqlSelectBuilder(this.consumer, query, "FROM " + table);
	}

	@Override
	public SelectQueryBuilder join(String table, Join join, String on) {
		return new MySqlSelectBuilder(
				this.consumer,
				query,
				joinToString(join) +" " + table + " ON " + on
		);
	}

	@Override
	public SelectQueryBuilder where(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "WHERE " + where);
	}

	@Override
	public SelectQueryBuilder andWhere(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "AND " + where);
	}

	@Override
	public SelectQueryBuilder orWhere(String where) {
		return new MySqlSelectBuilder(this.consumer, query, "OR (" + where + ")");
	}

	@Override
	public SelectQueryBuilder orderBy(String orderBy) {
		return new MySqlSelectBuilder(this.consumer, query, "ORDER BY " + orderBy);
	}

	@Override
	public SelectQueryBuilder groupBy(String groupBy) {
		return new MySqlSelectBuilder(this.consumer, query, "GROUP BY " + groupBy);
	}

	@Override
	public SelectQueryBuilder having(String having) {
		return new MySqlSelectBuilder(this.consumer, query, "HAVING " + having);
	}

	@Override
	public SelectQueryBuilder limit(int limit) {
		return new MySqlSelectBuilder(this.consumer, query, "LIMIT " + limit);
	}

	@Override
	public SelectQueryBuilder offset(int offset) {
		return new MySqlSelectBuilder(this.consumer, query, "OFFSET " + offset);
	}

	@Override
	public SelectQueryBuilder addParameter(String name, String value) {
		throw new NotImplementedYet();
	}

	@Override
	public String getSql() {
		return query;
	}

	@Override
	public String fetchSingle() throws SQLException {
		this.consumer.accept((conn)->{
			ResultSet res = conn.prepareStatement(query).executeQuery();
			if (res.next()) {
				single = res.getString(1);
			}
		});
		return single;
	}

	@Override
	public DatabaseRow fetchRow() throws SQLException {
		this.consumer.accept((conn)->{
			ResultSet res = conn.prepareStatement(query).executeQuery();
			row = new DatabaseRow();
			if (res.next()) {
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
					row.addValue(
						res.getMetaData().getColumnLabel(i),
						res.getString(i)
					);
				}
			}
		});
		return row;
	}

	@Override
	public List<DatabaseRow> fetchAll() throws SQLException {
		this.consumer.accept((conn)->{
			ResultSet res = conn.prepareStatement(query).executeQuery();
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
