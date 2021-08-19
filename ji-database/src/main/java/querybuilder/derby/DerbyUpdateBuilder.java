package querybuilder.derby;

import java.sql.Connection;

import query.buildersparent.QueryBuilderParent;
import query.wrappers.UpdateBuilder;

public class DerbyUpdateBuilder extends QueryBuilderParent implements UpdateBuilder {
	
	private boolean isFirst = true;
	
	public DerbyUpdateBuilder(final Connection connection, final String table) {
		super(connection);
		query.append("UPDATE " + table);
	}

	@Override
	public UpdateBuilder set(String set) {
		if (isFirst) {
			query.append(" SET " + set);
			isFirst = false;
		} else {
			query.append(", " + set);
		}
		return this;
	}

	@Override
	public UpdateBuilder where(String where) {
		query.append(" WHERE (" + where + ")");
		return this;
	}

	@Override
	public UpdateBuilder andWhere(String where) {
		query.append(" AND (" + where + ")");
		return this;
	}

	@Override
	public UpdateBuilder orWhere(String where) {
		query.append( " OR (" + where + ")");
		return this;
	}

	@Override
	public UpdateBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);;
		return this;
	}

}
