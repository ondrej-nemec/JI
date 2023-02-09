package ji.querybuilder.sqlite;

import java.sql.Connection;

import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.buildersparent.QueryBuilderParent;

public class SQLiteInsertBuilder extends QueryBuilderParent implements InsertBuilder {
	
	public SQLiteInsertBuilder(final Connection connection, final String table) {
		super(connection, Type.INSERT);
		query.append("INSERT INTO " + table);
	}

	@Override
	public InsertBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

}
