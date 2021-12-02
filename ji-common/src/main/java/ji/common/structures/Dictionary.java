package ji.common.structures;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface Dictionary<S> {

	/**
	 * 
	 * @param key
	 * @return value on given key in original type
	 */
	Object getValue(S key);
	
	void clear();
	
	/**
	 * 
	 * @param key
	 * @return value as DictionaryValue
	 */
	default DictionaryValue getDictionaryValue(S key) {
		return new DictionaryValue(getValue(key));
	}

	/**
	 * 
	 * @param key
	 * @return value on given key in original type
	 */
	default Object get(S key) {
		return getValue(key);
	}
	
	/**
	 * Check if value on given key is or can be instance of given class
	 * @param key
	 * @param clazz
	 * @return true if value can is/can be casted/can be parsed to clas
	 */
	default boolean is(S key, Class<?> clazz) {
		return getDictionaryValue(key).is(clazz);
	}
	
	default Boolean getBoolean(S key) {
		return getDictionaryValue(key).getBoolean();
	}
	
	default Byte getByte(S key) {
		return getDictionaryValue(key).getByte();
	}
	
	default Integer getInteger(S key) {
		return getDictionaryValue(key).getInteger();
	}
	
	default Long getLong(S key) {
		return getDictionaryValue(key).getLong();
	}
	
	default Float getFloat(S key) {
		return getDictionaryValue(key).getFloat();
	}
	
	default Double getDouble(S key) {
		return getDictionaryValue(key).getDouble();
	}
	
	default Character getCharacter(S key) {
		return getDictionaryValue(key).getCharacter();
	}
	
	default String getString(S key) {
		return getDictionaryValue(key).getString();
	}

	default LocalTime getTime(S key) {
		return getDictionaryValue(key).getTime();
	}
	
	default LocalTime getTime(S key, String pattern) {
		return getDictionaryValue(key).getTime(pattern);
	}
	
	default LocalDate getDate(S key) {
		return getDictionaryValue(key).getDate();
	}
	
	default LocalDate getDate(S key, String pattern) {
		return getDictionaryValue(key).getDate(pattern);
	}
	
	default LocalDateTime getDateTime(S key) {
		return getDictionaryValue(key).getDateTime();
	}
	
	default LocalDateTime getDateTime(S key, String pattern) {
		return getDictionaryValue(key).getDateTime(pattern);
	}
	
	default ZonedDateTime getDateTimeZone(S key) {
		return getDictionaryValue(key).getDateTimeZone();
	}
	
	default ZonedDateTime getDateTimeZone(S key, String pattern) {
		return getDictionaryValue(key).getDateTimeZone(pattern);
	}
	
	default <E extends Enum<E>> E getEnum(S key, Class<E> enumm) {
		return getDictionaryValue(key).getEnum(enumm);
	}
	
	default List<String> getList(S key, String delimiter) {
		return getDictionaryValue(key).getList(delimiter);
	}

	default <T> ListDictionary<T> getDictionaryList(S key) {
		return getDictionaryValue(key).getDictionaryList();
	}

	default <T, E> MapDictionary<T, E> getDictionaryMap(S key) {
		return getDictionaryValue(key).getDictionaryMap();
	}
	
	default <T> List<T> getList(S key) {
		return getDictionaryValue(key).getList();
	}

	default <K, V> Map<K, V> getMap(S key) {
		return getDictionaryValue(key).getMap();
	}

}
