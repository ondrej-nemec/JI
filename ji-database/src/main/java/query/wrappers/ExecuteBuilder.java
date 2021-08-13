package query.wrappers;

import java.sql.Connection;

import query.buildersparent.QueryBuilderParent;
import query.executors.SingleExecute;

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
