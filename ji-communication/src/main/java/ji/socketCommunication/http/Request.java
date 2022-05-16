package ji.socketCommunication.http;

import ji.common.structures.MapDictionary;

public class Request extends Exchange {
	
	private final HttpMethod method;
	private final String protocol;
	private final String uri; // full uri with parameter
	
	private String plainUri;
	private MapDictionary<String, Object> urlParams;
	
	public Request(HttpMethod method, String uri, String protocol) {
		super();
		this.method = method;
		this.protocol = protocol;
		this.uri = uri;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getUri() {
		return uri;
	}
	
	public String getPlainUri() {
		return plainUri;
	}
	
	public MapDictionary<String, Object> getUrlParameters() {
		return urlParams;
	}

	public void setUriParams(String plainUri, MapDictionary<String, Object> urlParams) {
		this.plainUri = plainUri;
		this.urlParams = urlParams;
	}
	
	@Override
	public String getFirstLine() {
		return String.format("%s %s %s", method, uri, protocol);
	}
	
}
