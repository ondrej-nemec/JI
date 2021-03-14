package testing.entities;

import java.util.HashMap;
import java.util.Map;

public class Row {

	private Map<String, Object> columns;
	
	public Row() {
		this.columns = new HashMap<>();
	}
	
	public Row addColumn(String columnName, Object value) {
		columns.put(columnName, value);
		return this;
	}
	
	public Map<String, Object> getColumns() {
		return columns;
	}
}
