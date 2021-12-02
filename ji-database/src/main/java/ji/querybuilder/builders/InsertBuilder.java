package ji.querybuilder.builders;

import java.util.Map;

import ji.querybuilder.buildersparent.ParametrizedBuilder;
import ji.querybuilder.executors.InsertExecute;

public interface InsertBuilder extends InsertExecute<InsertBuilder> {
	
	InsertBuilder addNotEscapedValue(String columnName, String value);
	
	default InsertBuilder addValue(String columnName, Object value) {
		// TODO use some static func here and in parametrized to remove this anonymous class ??
		new ParametrizedBuilder<InsertBuilder>() {
			@Override public String getSql() { return null; }
			@Override public Map<String, String> getParameters() { return null; }
			@Override public InsertBuilder addNotEscapedParameter(String name, String val) {
				addNotEscapedValue(name, val);
				return null;
			}
		}.addParameter(columnName, value);
		return this;
	}

}
