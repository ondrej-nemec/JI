package clientserver;

public enum StatusCode {
	
	CONTINUE(100, "CONTINUE"),
	OK(200, "OK");
	
	private final String description;
	
	private final int code;
	
	private StatusCode(int code, String descriptin) {
		this.code = code;
		this.description = descriptin;
	}
	
	@Override
	public String toString() {
		return String.format("%d %s", code, description);
	}
	
	public int getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}

}
