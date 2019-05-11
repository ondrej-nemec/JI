package database.support;

import java.util.HashMap;
import java.util.Map;

import exceptions.DatabaseException;

public class DatabaseRow {

	private Map<String, String> values;
	
	public DatabaseRow() {
		this.values = new HashMap<>();
	}
	
	public String getValue(final String name) {
		String value = values.get(name);
		if (value == null)
			throw new DatabaseException("Value for name '" + name + "' not found.");
		return value;
	}
	
	public void addValue(String key, String value) {
		values.put(key, value);
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
