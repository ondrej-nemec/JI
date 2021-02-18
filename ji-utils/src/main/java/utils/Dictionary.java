package utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public interface Dictionary {

	Object getValue(String name);
	
	default <T> T getValue(String name, Class<T> clazz) {
		Object value = getValue(name);
		if (value == null) {
			return null;
		}
		return clazz.cast(value);
	}
	
	default <T> T getValue(String name, Function<String, T> create) {
		Object value = getValue(name);
		if (value == null) {
			return null;
		}
	// TODO merge this two getValue to one 
		// if (value instanceof String) {}
		return create.apply(value.toString());
	}
	
	default Boolean getBoolean(String name) {
		return getValue(name, a->Boolean.parseBoolean(a));
	}
	
	default Integer getInteger(String name) {
		return getValue(name, a->Integer.parseInt(a));
	}
	
	default Double getDouble(String name) {
		return getValue(name, a->Double.parseDouble(a));
	}
	
	default Long getLong(String name) {
		return getValue(name, a->Long.parseLong(a));
	}
	
	default String getString(String name) {
		return getValue(name, a->a);
	}
	
	default <E extends Enum<E>> E getEnum(String name, Class<E> enumm) {
		return getValue(name, a->E.valueOf(enumm, a));
	}
	
	default List<String> getList(String name, String delimiter) {
		return getValue(name, a->Arrays.asList(a.split(delimiter)));
	}
	
}
