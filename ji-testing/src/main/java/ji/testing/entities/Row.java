package ji.testing.entities;

import java.util.HashMap;
import java.util.Map;

public class Row {

	private Map<String, Object> columns;
	private String idName;
	private Object idValue;
	
	public static Row insert() {
		return new Row(null, null);
	}
	
	public static Row update(String name, Object value) {
		return new Row(name, value);
	}
	
	private Row(String idName, Object value) {
		this.columns = new HashMap<>();
		this.idName = idName;
		this.idValue = value;
	}
	
	public Row addColumn(String columnName, Object value) {
		columns.put(columnName, value);
		return this;
	}
	
	public Map<String, Object> getColumns() {
		return columns;
	}
	
	public boolean isInsert() {
		return idName == null;
	}

	public String getIdName() {
		return idName;
	}

	public Object getIdValue() {
		return idValue;
	}
	
}
