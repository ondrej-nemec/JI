package querybuilder.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import common.Implode;
import querybuilder.CreateViewQueryBuilder;
import querybuilder.Join;

public class DerbyCreateViewBuilder implements CreateViewQueryBuilder {
	
	private final Connection connection;

	private final StringBuilder query;
	
	private final Map<String, String> params;
	
	private String dropView = null;
	
	public DerbyCreateViewBuilder(Connection connection, String name, boolean modify) {
		this.connection = connection;
		this.params = new HashMap<>();
		this.query = new StringBuilder();
		if (modify) {
			this.dropView = "DROP VIEW " + name;
		}
		query.append("CREATE VIEW " + name + " AS");
	}

	@Override
	public CreateViewQueryBuilder select(String... params) {
		query.append(" SELECT " + Implode.implode(", ", params));
		return this;
	}

	@Override
	public CreateViewQueryBuilder from(String table) {
		query.append(" FROM " + table);
		return this;
	}

	@Override
	public CreateViewQueryBuilder join(String table, Join join, String on) {
		query.append(" " + EnumToDerbyString.joinToString(join) +" " + table + " ON " + on);
		return this;
	}

	@Override
	public CreateViewQueryBuilder where(String where) {
		query.append(" WHERE " + where);
		return this;
	}

	@Override
	public CreateViewQueryBuilder andWhere(String where) {
		query.append(" AND " + where);
		return this;
	}

	@Override
	public CreateViewQueryBuilder orWhere(String where) {
		query.append(" OR (" + where + ")");
		return this;
	}

	@Override
	public CreateViewQueryBuilder orderBy(String orderBy) {
		query.append(" ORDER BY " + orderBy);
		return this;
	}

	@Override
	public CreateViewQueryBuilder groupBy(String groupBy) {
		query.append(" GROUP BY " + groupBy);
		return this;
	}

	@Override
	public CreateViewQueryBuilder having(String having) {
		query.append(" HAVING " + having);
		return this;
	}

	@Override
	public CreateViewQueryBuilder limit(int limit, int offset) {
		// OFFSET 0 ROWS FETCH NEXT 0 ROWS ONLY
		query.append(String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", offset, limit));
		return this;
	}

	@Override
	public CreateViewQueryBuilder addNotEscapedParameter(String name, String value) {
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

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			if (dropView != null) {
				stat.execute(dropView);
			}
			stat.executeUpdate(createSql());
		}
	}

}
