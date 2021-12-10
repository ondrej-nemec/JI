package ji.querybuilder.buildersparent;

import java.util.Map;

import ji.querybuilder.enums.SQL;

/**
 * Interface for bulders where you can pass parameters
 * @author Ondřej Němec
 *
 * @param <B>
 */
public interface ParametrizedBuilder<B> extends Builder {
	
	@Override
	default String createSql(Map<String, String> parameters) {
		Map<String, String> params = getParameters();
		params.putAll(parameters);
		String query = getSql();
		for (String name : params.keySet()) {
			query = query.replaceAll(name, params.get(name));
		}
		return query;
	}

	B addNotEscapedParameter(String name, String value);
	
	default B addParameter(String name, Object value) {
		return addNotEscapedParameter(name, SQL.escape(value));
	}
	
}
