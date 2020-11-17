package querybuilder;

import java.sql.SQLException;

public interface InsertQueryBuilder {
	/*
	InsertQueryBuilder addValue(String columnName, String value);
	
	InsertQueryBuilder addValue(String columnName, boolean value);
	
	InsertQueryBuilder addValue(String columnName, int value);
	
	InsertQueryBuilder addValue(String columnName, double value);
	
	InsertQueryBuilder addNullValue(String columnName);
	*/
	
	InsertQueryBuilder addNotEscapedValue(String columnName, String value);
	
	default InsertQueryBuilder addValue(String columnName, Object value) {
		if (value == null) {
			return addNotEscapedValue(columnName, "null");
		}
		Class<?> clazz = value.getClass();
		if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
			//  value ? "1" : "0"
			return addNotEscapedValue(columnName, value.toString());
		} else if (value instanceof Number) {
			return addNotEscapedValue(columnName, value.toString());
		} else if (clazz.isPrimitive() && !(clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(char.class))) {
			return addNotEscapedValue(columnName, value.toString());
		} else {
			return addNotEscapedValue(columnName, SQL.escape(value.toString()));
		}
	}
	
	String getSql();
	
	Object execute() throws SQLException;

}
