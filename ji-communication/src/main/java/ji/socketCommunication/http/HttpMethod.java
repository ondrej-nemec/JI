package ji.socketCommunication.http;

public enum HttpMethod {

	POST("POST"),
	GET("GET"),
	PUT("PUT"),
	DELETE("DELETE"),
	PATCH("PATCH");
	
	private final String method;
	
	private HttpMethod(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return method;
	}
}
