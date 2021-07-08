package common.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DictionaryValue {

	private final Object value;
	
	private Function<String, Object> stringMapping = (v)->{
		try {
			Object reader = Class.forName("json.JsonReader").newInstance();
			return reader.getClass().getMethod("read", String.class).invoke(reader, v);
		} catch (Exception e) {
			return v;
		}
	};
	private Function<String, Object> fromStringToListCallback = stringMapping;
	private Function<String, Object> fromStringToMapCallback = stringMapping;
	
	public DictionaryValue(Object value) {
		this.value = value;
	}
	
	public void addListCallback(Function<String, Object> fromStringToListCallback) {
		this.fromStringToListCallback = fromStringToListCallback;
	}
	
	public void addMapCallback(Function<String, Object> fromStringToMapCallback) {
		this.fromStringToMapCallback = fromStringToMapCallback;
	}
	
	/******/

	public Object getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> clazz) {
		if (value == null) {
			return null;
		}
		return (T)getParsedVal(clazz); // with clazz.cast cannot cast object to primitive
	}
	
	@SuppressWarnings("unchecked")
	private <E extends Enum<E>, T> Object getParsedVal(Class<T> clazz) {
		if (clazz.isInstance(value)) {
			return value;
		} else if (clazz.isAssignableFrom(Object.class)) {
			return value;
		} else if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
			return getBoolean();
		} else if (clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(byte.class)) {
			return getByte();
		} else if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class)) {
			return getShort();
		} else if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class)) {
			return getInteger();
		} else if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(long.class)) {
			return getLong();
		} else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(float.class)) {
			return getFloat();
		} else if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(double.class)) {
			return getDouble();
		} else if (clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(char.class)) {
			return getCharacter();
		} else if (clazz.isAssignableFrom(Map.class)) {
			return getMap();
		} else if (clazz.isAssignableFrom(List.class)) {
			return getList();
		} else if (clazz.isAssignableFrom(MapDictionary.class)) {
			return getDictionaryMap();
		} else if (clazz.isAssignableFrom(ListDictionary.class)) {
			return getDictionaryList();
		} else if (clazz.isEnum()) {
			return getEnum((Class<E>)clazz);
		} else if (value instanceof MapDictionary || value instanceof Map) { // TODO check very carefully if there is no recursion !
			return getDictionaryMap().parse(clazz);
		} else {
			return value;
		}
	}
	
	/**************/
	
	public Boolean getBoolean() {
		return parseValue(
			Boolean.class,
			v->v.toString().equalsIgnoreCase("true") || v.toString().equalsIgnoreCase("on") || v.toString().equalsIgnoreCase("1")
		);
	}
	
	public Byte getByte() {
		return parseValue(Byte.class, a->Byte.parseByte(a), v->Number.class.cast(v).byteValue());
	}
	
	public Short getShort() {
		return parseValue(Short.class, a->Short.parseShort(a), v->Number.class.cast(v).shortValue());
	}
	
	public Integer getInteger() {
		return parseValue(Integer.class, a->Integer.parseInt(a), v->Number.class.cast(v).intValue());
	}
	
	public Long getLong() {
		return parseValue(Long.class, a->Long.parseLong(a), v->Number.class.cast(v).longValue());
	}
	
	public Float getFloat() {
		return parseValue(Float.class, a->Float.parseFloat(a), v->Number.class.cast(v).floatValue());
	}
	
	public Double getDouble() {
		return parseValue(Double.class, a->Double.parseDouble(a), v->Number.class.cast(v).doubleValue());
	}
	
	public Character getCharacter() {
		return parseValue(Character.class, a->a.charAt(0), a->a.toString());
	}
	
	public String getString() {
		return parseValue(String.class, a->a, a->a.toString());
	}
	
	public <E extends Enum<E>> E getEnum(Class<E> enumm) {
		return parseValue(enumm, a->E.valueOf(enumm, a));
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(String delimiter) {
		return parseValue(List.class, a->Arrays.asList(a.split(delimiter)));
	}
	
	/************/

	@SuppressWarnings("unchecked")
	public <T> ListDictionary<T> getDictionaryList() {
		return parseValue(ListDictionary.class, fromStringToListCallback, (value)->{
			if (value instanceof List<?>) {
				return new ListDictionary<T>(List.class.cast(value));
			}
			return value;
		});
	}

	@SuppressWarnings("unchecked")
	public <T, E> MapDictionary<T, E> getDictionaryMap() {
		return parseValue(MapDictionary.class, fromStringToMapCallback, (value)->{
			if (value instanceof Map<?, ?>) {
				return new MapDictionary<T, E>(Map.class.cast(value));
			}
			return value;
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList() {
		return parseValue(List.class, fromStringToListCallback, (value)-> {
			if (value instanceof ListDictionary<?>) {
				return ListDictionary.class.cast(value).toList();
			}
			return value;
		});
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> getMap() {
		return parseValue(Map.class, fromStringToMapCallback, (value)-> {
			if (value instanceof MapDictionary<?, ?>) {
				return MapDictionary.class.cast(value).toMap();
			}
			return value;
		});
	}
	
	/***********/

	private <T> T parseValue(Class<T> clazz, Function<String, Object> fromString, Function<Object, Object> prepare) {
		if (value == null) {
			return null;
		}
		if (clazz.isInstance(value)) {
			return clazz.cast(value);
		}
		Object val = value;
		if (val instanceof String && fromString != null) {
			val = fromString.apply(val.toString());
		}
		if (prepare != null) {
			val = prepare.apply(val);
		}
		return clazz.cast(val);
	}
	
	private <T> T parseValue(Class<T> clazz, Function<String, Object> create) {
		return parseValue(clazz, create, null);
	}
	
	/************/

	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return value.equals(obj);
	}
}
