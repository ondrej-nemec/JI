package ji.common.structures;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DictionaryValue {

	private final Object value;
	
	private Function<String, Object> stringMapping = (v)->{
		try {
			Object reader = Class.forName("ji.json.JsonReader").getDeclaredConstructor().newInstance();
			return reader.getClass().getMethod("read", String.class).invoke(reader, v);
		} catch (Exception e) {
			return v;
		}
	};
	private Function<String, Object> fromStringToListCallback = stringMapping;
	private Function<String, Object> fromStringToMapCallback = stringMapping;
	private String dateTimePattern = null;
	private String onlyKey = null;
	
	public DictionaryValue(Object value) {
		this.value = value;
	}
	
	public DictionaryValue addListCallback(Function<String, Object> fromStringToListCallback) {
		this.fromStringToListCallback = fromStringToListCallback;
		return this;
	}
	
	public DictionaryValue addMapCallback(Function<String, Object> fromStringToMapCallback) {
		this.fromStringToMapCallback = fromStringToMapCallback;
		return this;
	}

	public DictionaryValue setDateTimeFormat(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
		return this;
	}

	public DictionaryValue setOnlyKey(String onlyKey) {
		this.onlyKey = onlyKey;
		return this;
	}
	
	/******/

	public Object getValue() {
		return value;
	}
	
	public boolean isPresent() {
		return value != null;
	}
	
	public boolean is(Class<?> clazz) {
		try {
			return clazz.isInstance(getValue(clazz));
		} catch (Exception e) {
			return false;
		}
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
		} else if (clazz.isAssignableFrom(Set.class)) {
			return getSet();
		} else if (clazz.isAssignableFrom(MapDictionary.class)) {
			return getDictionaryMap();
		} else if (clazz.isAssignableFrom(ListDictionary.class)) {
			return getDictionaryList();
		} else if (clazz.isEnum()) {
			return getEnum((Class<E>)clazz);
		} else if (clazz.isAssignableFrom(LocalDate.class)) {
			return getDate();
		} else if (clazz.isAssignableFrom(LocalTime.class)) {
			return getTime();
		} else if (clazz.isAssignableFrom(LocalDateTime.class)) {
			return getDateTime();
		} else if (clazz.isAssignableFrom(ZonedDateTime.class)) {
			return getDateTimeZone();
		} else if (value instanceof MapDictionary || value instanceof Map) { // check very carefully if there is no recursion !
			return getDictionaryMap().parse(clazz, onlyKey);
		} else {
			return value;
		}
	}
	
	/**************/
	
	public Boolean getBoolean() {
		return parseValue(
			Boolean.class,
			v->v.equalsIgnoreCase("true") || v.equalsIgnoreCase("on") || v.equalsIgnoreCase("1"),
			v->{
				if (v instanceof Number) {
					return Number.class.cast(v).byteValue() > 0;
				}
				return v;
			}
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
		return parseValue(Character.class, a->a.charAt(0), a->a.toString().charAt(0));
	}
	
	public String getString() {
		return parseValue(String.class, a->a, a->a.toString());
	}
	
	public <E extends Enum<E>> E getEnum(Class<E> enumm) {
		return parseValue(enumm, a->E.valueOf(enumm, a));
	}
	
	/*****************/
	
	public LocalTime getTime() {
		return getTime(dateTimePattern); // dateTimePattern == null ? "HH:mm:ss" : 
	}
	
	public LocalTime getTime(String pattern) {
		return getTimestamp(
			LocalTime.class, 
			time->LocalTime.from(time),
			string->LocalTime.parse(string, pattern == null ? DateTimeFormatter.ISO_TIME : DateTimeFormatter.ofPattern(pattern))
		);
	}
	
	public LocalDate getDate() {
		return getDate(dateTimePattern); // dateTimePattern == null ? "yyyy-MM-dd" : 
	}
	
	public LocalDate getDate(String pattern) {
		return getTimestamp(
			LocalDate.class, 
			time->LocalDate.from(time),
			string->LocalDate.parse(string, pattern == null ? DateTimeFormatter.ISO_DATE : DateTimeFormatter.ofPattern(pattern))
		);
	}
	
	public LocalDateTime getDateTime() {
		return getDateTime(dateTimePattern); // dateTimePattern == null ? "yyyy-MM-dd'T'HH-mm-ss.SSS" : 
	}
	
	public LocalDateTime getDateTime(String pattern) {
		return getTimestamp(
			LocalDateTime.class, 
			time->LocalDateTime.from(time), 
			(string)->LocalDateTime.parse(string, pattern == null ? DateTimeFormatter.ISO_DATE_TIME : DateTimeFormatter.ofPattern(pattern))
		);
	}
	
	public ZonedDateTime getDateTimeZone() {
		return getDateTimeZone(dateTimePattern); // dateTimePattern == null ? "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" : 
	}
	
	public ZonedDateTime getDateTimeZone(String pattern) {
		return getTimestamp(
			ZonedDateTime.class, 
			time->ZonedDateTime.from(time), 
			(string)->ZonedDateTime.parse(string, pattern == null ? DateTimeFormatter.ISO_ZONED_DATE_TIME : DateTimeFormatter.ofPattern(pattern))
		);
	}
	
	private <T> T getTimestamp(Class<T> clazz, Function<TemporalAccessor, T> fromTime, Function<String, T> fromString) {
		return parseValue(
			clazz,
			(string)->{
				try {
					return fromString.apply(string);
				} catch (Exception e) {
					return fromString.apply(string.replaceFirst(" ", "T"));
				}
			},
			(object)->{
				if (Long.class.isInstance(object) || long.class.isInstance(object)) {
					return fromTime.apply(Instant.ofEpochMilli(Long.class.cast(object)).atZone(ZoneId.systemDefault()));
				}
				if (TemporalAccessor.class.isInstance(object)) {
					return fromTime.apply(createZoneDateTime(object));
				}
				if (Date.class.isInstance(object)) {
					return fromTime.apply(Date.class.cast(object).toInstant().atZone(ZoneId.systemDefault()));
				}
				return fromString.apply(object.toString());
			}
		);
	}

	private ZonedDateTime createZoneDateTime(Object object) {
		if (LocalTime.class.isInstance(object)) {
			return ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.class.cast(object), ZoneId.systemDefault());
		}
		if (LocalDate.class.isInstance(object)) {
			return ZonedDateTime.of(LocalDate.class.cast(object), LocalTime.of(0, 0), ZoneId.systemDefault());
		}
		if (LocalDateTime.class.isInstance(object)) {
			return ZonedDateTime.of(LocalDateTime.class.cast(object), ZoneId.systemDefault());
		}
		if (ZonedDateTime.class.isInstance(object)) {
			return ZonedDateTime.class.cast(object);
		}
		return null;
	}

	/************/

	@SuppressWarnings("unchecked")
	public List<String> getList(String delimiter) {
		return parseValue(List.class, a->Arrays.asList(a.split(delimiter)));
	}

	@SuppressWarnings("unchecked")
	public <T> ListDictionary<T> getDictionaryList() {
		return parseValue(ListDictionary.class, fromStringToListCallback, (value)->{
			if (value instanceof Collection<?>) {
				return new ListDictionary<T>(Collection.class.cast(value));
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
			if (value instanceof Set<?>) {
				return new LinkedList<>(Set.class.cast(value));
			}
			return value;
		});
	}
	
	@SuppressWarnings("unchecked")
	public <T> Set<T> getSet() {
		return parseValue(Set.class, fromStringToListCallback, (value)-> {
			if (value instanceof ListDictionary<?> || value instanceof List<?>) {
				return new HashSet<>(getList());
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
