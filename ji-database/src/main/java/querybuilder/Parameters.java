package querybuilder;

public interface Parameters<B> {
	/*
	B addParameter(String name, boolean value);

	B addParameter(String name, int value);

	B addParameter(String name, double value);

	B addParameter(String name, String value);

	default B addParameter(String name, Object value) {
		return addParameter(name, value.toString());
	}
	*/
	
	B addNotEscapedParameter(String name, String value);
	
	default B addParameter(String name, Object value) {
		if (value == null) {
			return addNotEscapedParameter(name, "null");
		}
		Class<?> clazz = value.getClass();
		if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
			//  value ? "1" : "0"
			return addNotEscapedParameter(name, value.toString());
		} else if (value instanceof Number) {
			return addNotEscapedParameter(name, value.toString());
		} else if (clazz.isPrimitive() && !(clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(char.class))) {
			return addNotEscapedParameter(name, value.toString());
		} else {
			return addNotEscapedParameter(name, SQL.escape(value.toString()));
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

}
