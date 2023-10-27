package ji.querybuilder.builders;

import java.sql.Connection;

import ji.querybuilder.buildersparent.QueryBuilderParent;
import ji.querybuilder.executors.SingleExecute;

public class ExecuteBuilder extends QueryBuilderParent implements SingleExecute<ExecuteBuilder> {

	public ExecuteBuilder(Connection connection, String query) {
		super(connection);
		this.query.append(query);
	}

	@Override
	public ExecuteBuilder _addNotEscapedParameter(String name, String value) {
		_addNotEscaped(name, value);
		return this;
	}
	
}
