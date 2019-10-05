package clientserver;

import common.exceptions.LogicException;

public enum Method {

	POST("POST"),
	GET("GET"),
	PUT("PUT"),
	DELETE("DELETE");
	
	private final String method;
	
	private Method(String method) {
		this.method = method;
	}
	
	@Override
	public String toString() {
		return method;
	}
	
	public static Method fromString(String method) {
		if (method == null)
			throw new LogicException("Method could not be null");
		switch (method.toLowerCase()) {
		case "post": return Method.POST;
		case "get": return Method.GET;
		case "put": return Method.PUT;
		case "delete": return Method.DELETE;
		default: throw new LogicException("Unsupported method: " + method);
		}
	}
	
}
