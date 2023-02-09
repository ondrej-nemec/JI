package ji.querybuilder.sqlite;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import ji.querybuilder.builders.CreateViewBuilder;
import ji.querybuilder.buildersparent.Builder;

public class SQLiteCreateViewBuilder extends SelectWrapper<CreateViewBuilder> implements CreateViewBuilder {
	
	public SQLiteCreateViewBuilder(Connection connection, String name, boolean modify) {
		super(connection);
		if (modify) {
			query.append("DROP VIEW " + name + ";");
		}
		query.append("CREATE VIEW " + name + " AS ");
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

	@Override
	public List<Builder> _getBuilders() {
		return Arrays.asList(this);
	}

}
