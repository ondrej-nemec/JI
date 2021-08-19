package querybuilder.sqlserver;

import java.sql.Connection;

import query.wrappers.SelectBuilder;

public class SqlServerSelectBuilder extends SqlServerSelectWrapper<SelectBuilder> implements SelectBuilder {
	
	public SqlServerSelectBuilder(final Connection connection, final String... select) {
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
