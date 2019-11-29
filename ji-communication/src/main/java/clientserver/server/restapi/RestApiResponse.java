package clientserver.server.restapi;

import java.util.List;

import clientserver.StatusCode;

public class RestApiResponse {

	private final StatusCode statusCode;
	
	private final List<String> header;
	
	private final String message;

	public RestApiResponse(StatusCode statusCode, List<String> header, String message) {
		this.statusCode = statusCode;
		this.header = header;
		this.message = message;
	}

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public List<String> getHeader() {
		return header;
	}

	public String getMessage() {
		return message;
	}

}
