package json.event;

public class Value {

	private String val;
	
	private boolean isQuoted;
	
	public Value(String val, boolean isQuoted) {
		this.val = val;
		this.isQuoted = isQuoted;
	}
	
	public String getValue() {
		return val;
	}
	
	public ValueType getType() {
		if (isQuoted) {
			return ValueType.STRING;
		}
		if ("null".equals(val.toLowerCase())) {
			return ValueType.NULL;
		}
		if ("false".equals(val.toLowerCase()) || "true".equals(val.toLowerCase())) {
			return ValueType.BOOLEAN;
		}
		if (val.contains(".")) {
			return ValueType.DOUBLE;
		}
		return ValueType.INTEGER;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Value)) {
			return false;
		}
		Value v = (Value)obj;
		if (isQuoted != v.isQuoted) {
			return false;
		}
		if (!val.equals(v.val)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		if (isQuoted) {
			return String.format("\"%s\"", val);
		}
		return val;
	}
}
