package querybuilder.mysql;

import java.sql.Connection;

import query.buildersparent.QueryBuilderParent;
import query.wrappers.InsertBuilder;

public class MySqlInsertBuilder extends QueryBuilderParent implements InsertBuilder {
	
	public MySqlInsertBuilder(final Connection connection, final String table) {
		super(connection);
		query.append("INSERT INTO " + table);
	}

	@Override
	public InsertBuilder addNotEscapedValue(String columnName, String value) {
		params.put(columnName, value);
		return this;
	}

	@Override
	public String getSql() {
		String columns = "(";
		String values = "VALUES (";
		
		boolean first = true;
		for (String name : params.keySet()) {
			if (first) {
				first = false;
			} else {
				columns += ", ";
				values += ", ";
			}
			columns += name;
			values += params.get(name);
		}
		
		columns += ")";
		values += ")";
		return query + " " + columns + " " + values;
	}

}
