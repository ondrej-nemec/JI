package common.structures;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Dictionary<S> {

	Object getValue(S name);
	
	default DictionaryValue getDictionaryValue(S name) {
		return new DictionaryValue(getValue(name));
	}

	default Object get(S name) {
		return getValue(name);
	}
	
	default Boolean getBoolean(S name) {
		return getDictionaryValue(name).getBoolean();
	}
	
	default Byte getByte(S name) {
		return getDictionaryValue(name).getByte();
	}
	
	default Integer getInteger(S name) {
		return getDictionaryValue(name).getInteger();
	}
	
	default Long getLong(S name) {
		return getDictionaryValue(name).getLong();
	}
	
	default Float getFloat(S name) {
		return getDictionaryValue(name).getFloat();
	}
	
	default Double getDouble(S name) {
		return getDictionaryValue(name).getDouble();
	}
	
	default Character getCharacter(S name) {
		return getDictionaryValue(name).getCharacter();
	}
	
	default String getString(S name) {
		return getDictionaryValue(name).getString();
	}
	
	default Date getDate(S name) {
		return getDictionaryValue(name).getDate();
	}
	
	default <E extends Enum<E>> E getEnum(S name, Class<E> enumm) {
		return getDictionaryValue(name).getEnum(enumm);
	}
	
	default List<String> getList(S name, String delimiter) {
		return getDictionaryValue(name).getList(delimiter);
	}

	default <T> ListDictionary<T> getDictionaryList(S name) {
		return getDictionaryValue(name).getDictionaryList();
	}

	default <T, E> MapDictionary<T, E> getDictionaryMap(S name) {
		return getDictionaryValue(name).getDictionaryMap();
	}
	
	default <T> List<T> getList(S name) {
		return getDictionaryValue(name).getList();
	}

	default <K, V> Map<K, V> getMap(S name) {
		return getDictionaryValue(name).getMap();
	}

}
