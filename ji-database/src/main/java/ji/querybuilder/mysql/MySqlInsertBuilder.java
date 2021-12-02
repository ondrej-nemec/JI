package ji.querybuilder.mysql;

import java.sql.Connection;

import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.buildersparent.QueryBuilderParent;

public class MySqlInsertBuilder extends QueryBuilderParent implements InsertBuilder {
	
	public MySqlInsertBuilder(final Connection connection, final String table) {
		super(connection, Type.INSERT);
		query.append("INSERT INTO " + table);
	}

	@Override
	public InsertBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

}
