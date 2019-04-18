package testing.entities;

import java.util.HashMap;
import java.util.Map;

public class Row {

	private Map<String, String> columns;
	
	public Row() {
		this.columns = new HashMap<>();
	}
	
	public void addColumn(final String columnName, final Object value) {
		columns.put(columnName, "'" + value.toString() + "'");
	}
	
	public void addColumn(final String columnName, final Object value, final String function) {
		columns.put(columnName, function + "('" + value.toString() + "')");
	}
	
	public Map<String, String> getColumns() {
		return columns;
	}
}
