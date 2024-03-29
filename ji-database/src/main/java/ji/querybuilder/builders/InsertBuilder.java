package ji.querybuilder.builders;

import ji.querybuilder.enums.SQL;
import ji.querybuilder.executors.InsertExecute;

public interface InsertBuilder extends InsertExecute<InsertBuilder> {
	
	InsertBuilder _addNotEscapedValue(String columnName, String value);
	
	default InsertBuilder addValue(String columnName, Object value) {
		return _addNotEscapedValue(columnName, SQL.escape(value));
	}

}
