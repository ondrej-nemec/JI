package ji.socketCommunication.http;

import java.util.HashMap;
import java.util.Map;

import ji.common.structures.MapDictionary;

public class Request extends Exchange {
	
	private final HttpMethod method;
	private final String protocol;
	private final String uri; // full uri with parameter
	
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
		return uri; // TODO
	}
	
	public MapDictionary<String, Object> getUrlParameters() {
		return new MapDictionary<>(new HashMap<>()); // TODO
	}

	@Override
	public String getFirstLine() {
		return String.format("%s %s %s", method, uri, protocol);
	}
	
}
