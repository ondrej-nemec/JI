package ji.querybuilder.builders;

import java.sql.SQLException;
import java.util.List;

import ji.common.structures.DictionaryValue;
import ji.querybuilder.Builder;
import ji.querybuilder.builders.parents.PlainSelect;

public interface InsertBuilder extends Builder {
	
	InsertBuilder addValue(String columnName, Object value);
	
	InsertBuilder fromSelect(List<String> columns, PlainSelect<?> select);
	
	DictionaryValue execute() throws SQLException;

}
