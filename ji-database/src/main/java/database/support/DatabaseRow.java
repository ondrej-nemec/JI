package database.support;

import java.util.Map;

import exceptions.DatabaseException;

public class DatabaseRow {

	private Map<String, String> values;
	
	public String getValue(final String name) {
		String value = values.get(name);
		if (value == null)
			throw new DatabaseException("Value for name '" + name + "' not found.");
		return value;
	}
	
}
