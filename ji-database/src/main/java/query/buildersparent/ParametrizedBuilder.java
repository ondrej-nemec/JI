package query.buildersparent;

import java.util.Map;

import common.functions.Implode;
import common.structures.ListDictionary;
import querybuilder.SQL;

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
		if (value == null) {
			return addNotEscapedParameter(name, "null");
		}
		if (value instanceof Iterable<?>) {
			Iterable<?> iterable = Iterable.class.cast(value);
			return addNotEscapedParameter(
				name,
				Implode.implode(item->escapeParameterParameter(item), ",", iterable)
			);
		} else if (value instanceof ListDictionary) {
			ListDictionary<?> iterable = ListDictionary.class.cast(value);
			return addNotEscapedParameter(
				name,
				Implode.implode(item->escapeParameterParameter(item), ",", iterable.toList())
			);
		} else {
			return addNotEscapedParameter(name, escapeParameterParameter(value));
		}
	}

	default String escapeParameterParameter(Object value) {
		Class<?> clazz = value.getClass();
		if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
			//  value ? "1" : "0"
			return value.toString();
		} else if (value instanceof Number) {
			return value.toString();
		} else if (clazz.isPrimitive() && !(clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(char.class))) {
			return value.toString();
		} else {
			return SQL.escape(value.toString());
		}
	}
}
