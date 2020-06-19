package database.support;

import java.util.HashMap;
import java.util.Map;

public class DatabaseRow {

	private Map<String, String> values;
	
	public DatabaseRow() {
		this.values = new HashMap<>();
	}
	
	public String getValue(final String name) {
		return values.get(name);
	}
	
	public void addValue(String key, String value) {
		// to lower case - some db change column name
		values.put(key.toLowerCase(), value);
	}
	
	@Override
	public String toString() {
		String result = "{";
		for (String key : values.keySet()) {
			result += "[" + key + ": " + values.get(key) + "]";
		}
		
		return result + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! (obj instanceof DatabaseRow) )
			return false;
		DatabaseRow row = (DatabaseRow)obj;
		return values.equals(row.values);
		//return this.toString().equals(row.toString());
	}
	
}
