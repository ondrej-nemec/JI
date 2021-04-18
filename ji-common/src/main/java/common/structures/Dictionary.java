package common.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Dictionary<S> {

	Object getValue(S name);

	default Object get(S name) {
		return getValue(name);
	}
	
	default <T> T getValue(S name, Class<T> clazz) {
		Object value = getValue(name);
		if (value == null) {
			return null;
		}
		return clazz.cast(value);
	}
	
	default <T> T getValue(S name, Function<String, T> create) {
		Object value = getValue(name);
		if (value == null) {
			return null;
		}
	// TODO merge this two getValue to one 
		// if (value instanceof String) {}
		return create.apply(value.toString());
	}
	
	default Boolean getBoolean(S name) {
		return getValue(name, a->Boolean.parseBoolean(a));
	}
	
	default Integer getInteger(S name) {
		return getValue(name, a->Integer.parseInt(a));
	}
	
	default Double getDouble(S name) {
		return getValue(name, a->Double.parseDouble(a));
	}
	
	default Long getLong(S name) {
		return getValue(name, a->Long.parseLong(a));
	}
	
	default String getString(S name) {
		return getValue(name, a->a);
	}
	
	default <E extends Enum<E>> E getEnum(S name, Class<E> enumm) {
		return getValue(name, a->E.valueOf(enumm, a));
	}
	
	default List<String> getList(S name, String delimiter) {
		return getValue(name, a->Arrays.asList(a.split(delimiter)));
	}

	@SuppressWarnings("unchecked")
	default <T> ListDictionary<T> getAsDictionaryList(S name) {
		return new ListDictionary<T>(getValue(name, a->List.class.cast(a)));
	}

	@SuppressWarnings("unchecked")
	default <T, E> MapDictionary<T, E> getAsDictionaryMap(S name) {
		return new MapDictionary<T, E>(getValue(name, a->Map.class.cast(a)));
	}

	@SuppressWarnings("unchecked")
	default <T> ListDictionary<T> getDictionaryList(S name) {
		return getValue(name, a->ListDictionary.class.cast(a));
	}

	@SuppressWarnings("unchecked")
	default <T, E> MapDictionary<T, E> getDictionaryMap(S name) {
		return getValue(name, a->MapDictionary.class.cast(a));
	}
	
/*	
	@SuppressWarnings("unchecked")
	default <T> List<T> getList(S name) {
		return getValue(name, a->List.class.cast(a));
	}
	
	default Dictionary getMap(S name) {
		return getValue(name, a->Dictionary.class.cast(a));
	}
*/
}
