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
	
	@Override
	public String toString() {
		return code + " " + message + "\r\n" + content;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RestAPIResponse) ) {
			return false;
		}
		RestAPIResponse r = (RestAPIResponse)obj;
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
