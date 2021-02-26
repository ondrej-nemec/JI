package querybuilder.sqlserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import common.Implode;
import querybuilder.CreateViewQueryBuilder;
import querybuilder.Join;

public class SqlServerCreateViewBuilder implements CreateViewQueryBuilder {
	
	private final Connection connection;

	private final StringBuilder query;
	
	private final Map<String, String> params;
	
	public SqlServerCreateViewBuilder(Connection connection, String name, boolean modify) {
		this.connection = connection;
		this.params = new HashMap<>();
		this.query = new StringBuilder();
		if (modify) {
			query.append("ALTER VIEW " + name + " AS");
		} else {
			query.append("CREATE VIEW " + name + " AS");
		}
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
		query.append(" " + EnumToSqlServerString.joinToString(join) +" " + table + " ON " + on);
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
	    query.append(" OFFSET " + offset + "ROWS FETCH NEXT " + limit + " ROWS ONLY");
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

	@Override
	public void execute() throws SQLException {
		try (Statement stat = connection.createStatement();) {
			stat.executeUpdate(createSql());
		}
	}

}
