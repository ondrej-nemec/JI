package ji.querybuilder.derby;

import java.sql.Connection;

import ji.querybuilder.builders.SelectBuilder;

public class DerbySelectBuilder extends SelectWrapper<SelectBuilder> implements SelectBuilder {

	public DerbySelectBuilder(final Connection connection, final String... select) {
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
