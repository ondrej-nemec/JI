package querybuilder.postgresql;

import java.sql.Connection;

import querybuilder.builders.SelectBuilder;

public class PostgreSqlSelectBuilder extends PostgresSqlSelectWrapper<SelectBuilder> implements SelectBuilder {
	
	public PostgreSqlSelectBuilder(final Connection connection, final String... select) {
		super(connection);
		select(select);
	}

	@Override
	public SelectBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

	@Override
	protected SelectBuilder get() {
		return this;
	}


}
