package querybuilder.derby;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import common.Implode;
import database.support.DatabaseRow;
import querybuilder.Join;
import querybuilder.SelectQueryBuilder;

public class DerbySelectBuilder implements SelectQueryBuilder {

	private final StringBuilder query;
	
	private final Map<String, String> params;
	
	private final Connection connection;
	
	public DerbySelectBuilder(final Connection connection, final String... select) {
		this("SELECT " + Implode.implode(", ", select), connection);
	}

	protected DerbySelectBuilder(final String query, final Connection connection) {
		this.connection = connection;
		this.query = new StringBuilder(query);
		this.params = new HashMap<>();
	}
	
	@Override
	public SelectQueryBuilder from(String table) {
		query.append(" FROM " + table);
		return this;
	}

	@Override
	public SelectQueryBuilder join(String table, Join join, String on) {
		query.append(" " + EnumToDerbyString.joinToString(join) +" " + table + " ON " + on);
		return this;
	}

	@Override
	public SelectQueryBuilder where(String where) {
		query.append(" WHERE (" + where + ")");
		return this;
	}

	@Override
	public SelectQueryBuilder andWhere(String where) {
		query.append(" AND (" + where + ")");
		return this;
	}

	@Override
	public SelectQueryBuilder orWhere(String where) {
		query.append(" OR (" + where + ")");
		return this;
	}

	@Override
	public SelectQueryBuilder orderBy(String orderBy) {
		query.append(" ORDER BY " + orderBy);
		return this;
	}

	@Override
	public SelectQueryBuilder groupBy(String groupBy) {
		query.append(" GROUP BY " + groupBy);
		return this;
	}

	@Override
	public SelectQueryBuilder having(String having) {
		query.append(" HAVING " + having);
		return this;
	}

	@Override
	public SelectQueryBuilder limit(int limit, int offset) {
		query.append(String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", offset, limit));
		return this;
	}

	@Override
	public SelectQueryBuilder addNotEscapedParameter(String name, String value) {
		params.put(name, value);
		return this;
	}

	@Override
	public String getSql() {
		return query.toString();
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
	public Object fetchSingle() throws SQLException {
		try (Statement stat = connection.createStatement(); ResultSet res  = stat.executeQuery(createSql());) {
			Object result = null;
			if (res.next()) {
				result = res.getObject(1);
    		}
    		return result;
		}
	 }

	@Override
	public DatabaseRow fetchRow() throws SQLException {
		try (Statement stat = connection.createStatement(); ResultSet res  = stat.executeQuery(createSql());) {
			DatabaseRow row = new DatabaseRow();
    		if (res.next()) {
    			for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
    				row.addValue(
    					res.getMetaData().getColumnLabel(i),
    					res.getObject(i)
    				);
    			}
    		}
    		return row;
		}
	}

	@Override
	public List<DatabaseRow> fetchAll() throws SQLException {
		try (Statement stat = connection.createStatement(); ResultSet res  = stat.executeQuery(createSql());) {
			List<DatabaseRow> rows = new LinkedList<>();
    		while (res.next()) {
    			DatabaseRow row = new DatabaseRow();
    			for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
    				row.addValue(
    					res.getMetaData().getColumnLabel(i),
    					res.getObject(i)
    				);
    			}
    			rows.add(row);
    		}		
    		return rows;
		}
	}
/*
	@Override
	public void fetchAll(Consumer<DatabaseRow> consumer) throws SQLException {
		try (Statement stat = connection.createStatement(); ResultSet res  = stat.executeQuery(createSql());) {
			while (res.next()) {
    			DatabaseRow row = new DatabaseRow();
    			for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
    				row.addValue(
    					res.getMetaData().getColumnLabel(i),
    					res.getString(i)
    				);
    			}
    			consumer.accept(row);
    		}
		}
	}
*/
	@Override
	public List<String> fetchAll(Function<DatabaseRow, String> function) throws SQLException {
		List<String> result = new LinkedList<>();
		for(DatabaseRow row : fetchAll()) {
			result.add(function.apply(row));
		}
		return result;
	}
/*	
	protected String joinToString(final Join join) {
		switch(join) {
			case FULL_OUTER_JOIN: throw new RuntimeException("Full Outer Join is not supported by mysql");
			case INNER_JOIN: return "JOIN";
			case LEFT_OUTER_JOIN: return "LEFT JOIN";
			case RIGHT_OUTER_JOIN: return "RIGHT JOIN";
			default: throw new RuntimeException("Not implemented join: " + join);
		}
	}
*/
}
