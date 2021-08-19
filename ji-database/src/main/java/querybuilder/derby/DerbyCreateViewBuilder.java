package querybuilder.derby;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import query.buildersparent.Builder;
import query.buildersparent.SimpleBuilder;
import query.wrappers.CreateViewBuilder;

public class DerbyCreateViewBuilder extends SelectWrapper<CreateViewBuilder> implements CreateViewBuilder {
	
	private final SimpleBuilder drop = new SimpleBuilder();
	
	public DerbyCreateViewBuilder(Connection connection, String name, boolean modify) {
		super(connection);
		if (modify) {
			drop.append("DROP VIEW " + name);
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
	
	// for test
	protected SimpleBuilder getDrop() {
		return drop;
	}

	@Override
	public List<Builder> _getBuilders() {
		return Arrays.asList(drop, this);
	}
}
