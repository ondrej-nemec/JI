package socketCommunication.http.client;

public class RestApiResponse {

	private final int code;
	
	private final String message;
	
	private final String content;

	public RestApiResponse(int code, String message, String content) {
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
	
	@Override
	public String toString() {
		return code + " " + message + "\r\n" + content;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RestApiResponse) ) {
			return false;
		}
		RestApiResponse r = (RestApiResponse)obj;
		if (code != r.code) {
			return false;
		}
		if (!message.equals(r.message)) {
			return false;
		}
		if (!content.equals(r.content)) {
			return false;
		}
		return true;
	}

}
