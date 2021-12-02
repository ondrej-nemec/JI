package ji.querybuilder.sqlserver;

import ji.querybuilder.builders.Functions;
import ji.querybuilder.enums.ColumnType;

public class SqlServerFunctions implements Functions {

	@Override
	public String concat(String param, String... params) {
		StringBuilder b = new StringBuilder("CONCAT(");
		b.append(param);
		for (String p : params) {
			b.append(", " + p);
		}
		b.append(")");
		return b.toString();
	}

	/**
	 * Param(s) must be column name or escaped value - you can use parameters
	 * @return
	 */
	// TODO test this method
	@Override
	public String cast(String param, ColumnType type) {
		return "CAST(" + param + " AS " + EnumToSqlServerString.typeToString(type) + ")";
	}

}
