package query.wrappers;

import java.util.Map;

import query.buildersparent.ParametrizedBuilder;
import query.executors.InsertExecute;

public interface InsertBuilder extends InsertExecute<InsertBuilder> {
	
	InsertBuilder addNotEscapedValue(String columnName, String value);
	
	default InsertBuilder addValue(String columnName, String value) {
		// TODO use some static func here and in parametrized to remove this anonymous class ??
		new ParametrizedBuilder<InsertBuilder>() {
			@Override public String getSql() { return null; }
			@Override public Map<String, String> getParameters() { return null; }
			@Override public InsertBuilder addNotEscapedParameter(String name, String val) {
				addNotEscapedValue(name, val);
				return null;
			}
		}.addNotEscapedParameter(columnName, value);
		return this;
	}

}
