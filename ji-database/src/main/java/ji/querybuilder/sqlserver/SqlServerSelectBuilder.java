package ji.querybuilder.sqlserver;

import java.sql.Connection;

import ji.querybuilder.builders.SelectBuilder;

public class SqlServerSelectBuilder extends SqlServerSelectWrapper<SelectBuilder> implements SelectBuilder {
	
	public SqlServerSelectBuilder(final Connection connection, final String... select) {
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
