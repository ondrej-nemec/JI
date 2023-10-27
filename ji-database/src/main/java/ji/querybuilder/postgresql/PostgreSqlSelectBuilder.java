package ji.querybuilder.postgresql;

import java.sql.Connection;

import ji.querybuilder.builders.SelectBuilder;

public class PostgreSqlSelectBuilder extends PostgresSqlSelectWrapper<SelectBuilder> implements SelectBuilder {
	
	public PostgreSqlSelectBuilder(final Connection connection, final String... select) {
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


}
