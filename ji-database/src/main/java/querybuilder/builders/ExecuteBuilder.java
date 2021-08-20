package querybuilder.builders;

import java.sql.Connection;

import querybuilder.buildersparent.QueryBuilderParent;
import querybuilder.executors.SingleExecute;

public class ExecuteBuilder extends QueryBuilderParent implements SingleExecute<ExecuteBuilder> {

	public ExecuteBuilder(Connection connection, String query) {
		super(connection);
		this.query.append(query);
	}

	@Override
	public ExecuteBuilder addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}
	
}
