package database.support;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DatabaseRow {

	private Map<String, Object> values;
	
	public DatabaseRow() {
		this.values = new HashMap<>();
	}
	
	public Object getValue(final String name) {
		return values.get(name);
	}
	
	private <T> T getValue(String name, Function<String, T> create) {
		Object value = getValue(name);
		if (value == null) {
			return null;
		}
		return create.apply(value.toString());
	}
	
	public String getString(String name) {
		return getValue(name, a->a);
	}
	
	public Integer getInt(String name) {
		return getValue(name, a->Integer.parseInt(a));
	}
	
	public Boolean getBoolean(String name) {
		return getValue(name, a->Boolean.parseBoolean(a));
	}
	
	public Long getLong(String name) {
		return getValue(name, a->Long.parseLong(a));
	}
	
	public Double getDouble(String name) {
		return getValue(name, a->Double.parseDouble(a));
	}
	
	public void addValue(String key, Object value) {
		// to lower case - some db change column name
		values.put(key.toLowerCase(), value);
	}
	
	public Map<String, Object> getValues() {
		return values;
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
