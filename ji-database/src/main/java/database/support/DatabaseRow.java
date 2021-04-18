package database.support;

import java.util.HashMap;
import java.util.Map;

import common.structures.MapDictionary;

public class DatabaseRow extends MapDictionary<String, Object> {

	public DatabaseRow() {
		super(new HashMap<>());
	}
	
	public void addValue(String key, Object value) {
		// to lower case - some db change column name
		put(key.toLowerCase(), value);
	}
	
	public Map<String, Object> getValues() {
		return toMap();
	}
	
}
