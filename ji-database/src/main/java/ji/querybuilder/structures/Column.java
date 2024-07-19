package ji.querybuilder.structures;

import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;

public class Column {

	private final String name;
	private final ColumnType type;
	private final Object value;
	private final ColumnSetting[] settings;
	
	public Column(String name, ColumnType type, Object value, ColumnSetting[] settings) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.settings = settings;
	}

	public String getName() {
		return name;
	}

	public ColumnType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public ColumnSetting[] getSettings() {
		return settings;
	}
	
	public String getOldName() {
		return name;
	}
	
	public String getNewName() {
		return value.toString();
	}

}
