package querybuilder.derby;

import java.sql.Connection;

import query.buildersparent.QueryBuilderParent;
import query.wrappers.InsertBuilder;

public class DerbyInsertBuilder extends QueryBuilderParent implements InsertBuilder {

	public DerbyInsertBuilder(final Connection connection, final String table) {
		super(connection, Type.INSERT);
		query.append("INSERT INTO " + table);
	}

	@Override
	public InsertBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

}
