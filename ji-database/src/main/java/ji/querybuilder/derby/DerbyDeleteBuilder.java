package ji.querybuilder.derby;

import java.sql.Connection;

import ji.querybuilder.builders.DeleteBuilder;
import ji.querybuilder.buildersparent.QueryBuilderParent;

public class DerbyDeleteBuilder extends QueryBuilderParent implements DeleteBuilder {
	
	public DerbyDeleteBuilder(final Connection connection, final String table) {
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
	public DeleteBuilder _addNotEscapedParameter(String name, String value) {
		params.put(name, value);
		return this;
	}
}
