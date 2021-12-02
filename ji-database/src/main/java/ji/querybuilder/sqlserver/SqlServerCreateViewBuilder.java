package ji.querybuilder.sqlserver;

import java.sql.Connection;

import ji.querybuilder.builders.CreateViewBuilder;

public class SqlServerCreateViewBuilder extends SqlServerSelectWrapper<CreateViewBuilder> implements CreateViewBuilder {

	public SqlServerCreateViewBuilder(Connection connection, String name, boolean modify) {
		super(connection);
		if (modify) {
			mainBuilder.append("ALTER VIEW " + name + " AS ");
		} else {
			mainBuilder.append("CREATE VIEW " + name + " AS ");
		}
	}

	@Override
	public CreateViewBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}

	@Override
	protected CreateViewBuilder get() {
		return this;
	}

}
