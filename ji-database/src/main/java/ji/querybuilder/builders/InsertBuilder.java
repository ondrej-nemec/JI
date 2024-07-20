package ji.querybuilder.builders;

import java.sql.SQLException;

import ji.common.structures.DictionaryValue;
import ji.querybuilder.Builder;
import ji.querybuilder.builders.parents.PlainSelect;
import ji.querybuilder.structures.SubSelect;

public interface InsertBuilder extends Builder {
	
	InsertBuilder with(String name, SubSelect select);
	
	InsertBuilder addValue(String columnName, Object value);
	
	InsertBuilder fromSelect(PlainSelect<?> select);
	
	DictionaryValue execute() throws SQLException;

}
