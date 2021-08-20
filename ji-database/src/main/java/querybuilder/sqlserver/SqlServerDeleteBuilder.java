package querybuilder.sqlserver;

import java.sql.Connection;

import querybuilder.builders.DeleteBuilder;
import querybuilder.buildersparent.QueryBuilderParent;

public class SqlServerDeleteBuilder extends QueryBuilderParent implements DeleteBuilder {
	
	public SqlServerDeleteBuilder(final Connection connection, final String table) {
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
		_addNotEscaped(name, value);
		return this;
	}

}
