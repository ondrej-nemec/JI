package clientserver;

public enum Method {

	POST("POST"),
	GET("GET"),
	PUT("PUT"),
	DELETE("DELETE"),
	PATCH("PATCH");
	
	private final String method;
	
	private Method(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return method;
	}
}
