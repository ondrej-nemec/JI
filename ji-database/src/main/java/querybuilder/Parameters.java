package querybuilder;

import common.functions.Implode;
import common.structures.ListDictionary;

public interface Parameters<B> extends Batch {
	
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
	
	/**
	 * @return SQL string where are auxiliary names for variables
	 */
	String getSql();
	
	/**
	 * @return SQL string where auxiliary names are replaced with values
	 */
	String createSql();
	
	@Override
	default String getQuerySql() {
		return createSql();
	}

}
