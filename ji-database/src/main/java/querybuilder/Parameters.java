package querybuilder;

import java.util.List;

public interface Parameters<B> extends Batch {
	
	B addNotEscapedParameter(String name, String value);
	
	@SuppressWarnings("unchecked")
	default B addParameter(String name, Object value) {
		if (value == null) {
			return addNotEscapedParameter(name, "null");
		}
		if (value instanceof List) {
			StringBuilder b = new StringBuilder();
			List.class.cast(value).forEach((item)->{
				if (!b.toString().isEmpty()) {
					b.append(",");
				}
				b.append(escapeParameterParameter(item));
			});
			return addNotEscapedParameter(name, b.toString());
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
