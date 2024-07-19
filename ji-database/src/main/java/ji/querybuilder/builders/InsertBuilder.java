package ji.querybuilder.builders;

import java.sql.SQLException;
import java.util.function.Function;

import ji.common.structures.DictionaryValue;
import ji.querybuilder.Builder;
import ji.querybuilder.Functions;
import ji.querybuilder.builders.share.PlainSelect;

public interface InsertBuilder extends Builder {
	
	default InsertBuilder addValue(String columnName, Object value) {
		return addValue(columnName, f->value);
	}
	
	InsertBuilder addValue(String columnName, Function<Functions, Object> value);
	
	InsertBuilder fromSelect(PlainSelect<?> select);
	
	DictionaryValue execute() throws SQLException;

}
