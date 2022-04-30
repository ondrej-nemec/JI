package ji.socketCommunication.http;

public class Response extends Exchange {

	private final String protocol;
	private final StatusCode code;
	
	public Response(StatusCode code, String protocol) {
		super();
		this.protocol = protocol;
		this.code = code;
	}

	public String getProtocol() {
		return protocol;
	}

	public StatusCode getCode() {
		return code;
	}

	@Override
	public String getFirstLine() {
		return String.format("%s %s %s", protocol, code.getCode(), code.getDescription());
	}
	
}
