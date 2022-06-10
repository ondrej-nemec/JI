package ji.querybuilder.enums;

public class ColumnType {
	
	public enum Type {
		BOOLEAN,
		INT,
		STRING,
		TEXT,
		DATETIME,
		DATE,
		TIME,
		CHAR
	}

	public static ColumnType bool() {
		return new ColumnType(Type.BOOLEAN);
	}
	
	public static ColumnType integer() {
		return new ColumnType(Type.INT);
	}

	public static ColumnType charType(int size) {
		return new ColumnType(Type.CHAR, size);
	}
	
	public static ColumnType text() {
		return new ColumnType(Type.TEXT);
	}
	
	public static ColumnType string(int size) {
		return new ColumnType(Type.STRING, size);
	}
	
	public static ColumnType datetime() {
		return new ColumnType(Type.DATETIME);
	}
	
	private int size;
	
	private final Type type;
	
	private ColumnType(Type type) {
		this.type = type;
	}
	
	private ColumnType(Type type, int size) {
		this.type = type;
		this.size = size;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
}
