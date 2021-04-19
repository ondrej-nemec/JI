package common.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DictionaryValue {

	private final Object value;
	
	public DictionaryValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
	public <T> T getValue(Class<T> clazz, Function<Object, Object> prepare) {
		if (value == null) {
			return null;
		}
		if (clazz.isInstance(value)) {
			return clazz.cast(value);
		}
		return clazz.cast(prepare.apply(value));
	}
	
	public <T> T parseValue(Function<String, T> create) {
		if (value == null) {
			return null;
		}
		return create.apply(value.toString());
	}
	
	public Boolean getBoolean() {
		return parseValue(a->Boolean.parseBoolean(a));
	}
	
	public Integer getInteger() {
		return parseValue(a->Integer.parseInt(a));
	}
	
	public Double getDouble() {
		return parseValue(a->Double.parseDouble(a));
	}
	
	public Long getLong() {
		return parseValue(a->Long.parseLong(a));
	}
	
	public String getString() {
		return parseValue(a->a);
	}
	
	public <E extends Enum<E>> E getEnum(Class<E> enumm) {
		return parseValue(a->E.valueOf(enumm, a));
	}
	
	public List<String> getList(String delimiter) {
		return parseValue(a->Arrays.asList(a.split(delimiter)));
	}

	@SuppressWarnings("unchecked")
	public <T> ListDictionary<T> getDictionaryList() {
		return getValue(ListDictionary.class, (value)->{
			if (value instanceof List<?>) {
				return new ListDictionary<T>(List.class.cast(value));
			}
			return value;
		});
	}

	@SuppressWarnings("unchecked")
	public <T, E> MapDictionary<T, E> getDictionaryMap() {
		return getValue(MapDictionary.class, (value)->{
			if (value instanceof Map<?, ?>) {
				return new MapDictionary<T, E>(Map.class.cast(value));
			}
			return value;
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList() {
		return getValue(List.class, (value)-> {
			if (value instanceof ListDictionary<?>) {
				return ListDictionary.class.cast(value).toList();
			}
			return value;
		});
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap() {
		return getValue(Map.class, (value)-> {
			if (value instanceof MapDictionary<?, ?>) {
				return MapDictionary.class.cast(value).toMap();
			}
			return value;
		});
	}
}
