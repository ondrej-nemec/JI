package clientserver.client;

public class RestAPIResponse {

	private final int code;
	
	private final String message;
	
	private final String content;

	public RestAPIResponse(int code, String message, String content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getContent() {
		return content;
	}

}
