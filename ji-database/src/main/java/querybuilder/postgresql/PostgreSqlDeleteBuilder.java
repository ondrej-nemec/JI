package querybuilder.postgresql;

import java.sql.Connection;

import query.buildersparent.QueryBuilderParent;
import query.wrappers.DeleteBuilder;

public class PostgreSqlDeleteBuilder extends QueryBuilderParent implements DeleteBuilder {

	public PostgreSqlDeleteBuilder(final Connection connection, final String table) {
		super(connection);
		query.append("DELETE FROM " + table);
	}

	@Override
	public DeleteBuilder where(String where) {
		query.append(" WHERE (" + where + ")");
		return this;
	}

	@Override
	public DeleteBuilder andWhere(String where) {
		query.append(" AND (" + where + ")");
		return this;
	}

	@Override
	public DeleteBuilder orWhere(String where) {
		query.append(" OR (" + where + ")");
		return this;
	}

	@Override
	public DeleteBuilder addNotEscapedParameter(String name, String value) {
		params.put(name, value);
		return this;
	}

}
