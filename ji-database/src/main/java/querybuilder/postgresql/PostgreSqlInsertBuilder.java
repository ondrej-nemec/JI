package querybuilder.postgresql;

import java.sql.Connection;

import querybuilder.builders.InsertBuilder;
import querybuilder.buildersparent.QueryBuilderParent;

public class PostgreSqlInsertBuilder extends QueryBuilderParent implements InsertBuilder {
	
	public PostgreSqlInsertBuilder(final Connection connection, final String table) {
		super(connection, Type.INSERT);
		query.append("INSERT INTO " + table);
	}

	@Override
	public InsertBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

}
