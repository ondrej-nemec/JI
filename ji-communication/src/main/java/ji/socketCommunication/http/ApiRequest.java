package ji.socketCommunication.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ji.common.structures.DictionaryValue;
import ji.common.structures.MapDictionary;

public class ApiRequest {

	private final String protocol; // TODO enum?
	
	private String fullUrl; // for request only	
	private HttpMethod method; // for request only
	private String url; // for request only
	
	private StatusCode code; // for response only
	
	private Map<String, List<Object>> headers = new HashMap<>();
	private MapDictionary<String, Object> urlParamters = MapDictionary.hashMap();
	private MapDictionary<String, Object> bodyParamters = MapDictionary.hashMap();
	private String body;
	
	// request response
	public ApiRequest(StatusCode code, String protocol) {
		this.protocol = protocol;
		this.code = code;
	}
	
	// server accept request
	public ApiRequest(HttpMethod method, String fullUrl, String protocol) {
		this.fullUrl = fullUrl;
		this.protocol = protocol;
		this.method = method;
	}
	
	/**** headers section *****/
	
	public Map<String, List<Object>> getHeaders() {
		return headers;
	}
	
	public void addHeader(String name, Object value) {
		if (!headers.containsKey(name)) {
			headers.put(name, new LinkedList<>());
		}
		headers.get(name).add(value);
	}
	
	public List<Object> getHeaders(String name) {
		return headers.get(name);
	}
	
	public Object getHeader(String name)  {
		if (headers.containsKey(name) && headers.get(name).size() > 0) {
			return headers.get(name).get(0);
		}
		return null;
	}
	
	public <T> T getHeader(String name, Class<T> clazz) {
		return new DictionaryValue(getHeader(name)).getValue(clazz);
	}
	/**** parameters and body section ****/
	
	public void addUrlParameter(String name, Object value) {
		urlParamters.put(name, value);
	}
	
	public DictionaryValue getUrlParameter(String name) {
		return urlParamters.getDictionaryValue(name);
	}
	
	public void addBodyParameter(String name, Object value) {
		bodyParamters.put(name, value);
	}
	
	public DictionaryValue getBodyParameter(String name) {
		return bodyParamters.getDictionaryValue(name);
	}
	
	public void addBodyParameters(Map<String, Object> params) {
		bodyParamters.putAll(params);
	}
	
	public Map<String, Object> getBodyParameters() {
		return bodyParamters.toMap();
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}
	/*
	public RequestParameters getRequestParameters() {
		// TODO
	}
	*/
	/** first line section ***/
	
	public StatusCode getStatusCode() {
		return code;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getUrl() {
		return url;
	}

	public String getFullUrl() {
		return fullUrl;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (code == null) { // request
			b.append(method);
			b.append(" ");
			b.append(fullUrl);
			b.append(" ");
			b.append(protocol);
		} else { // response
			b.append(protocol);
			b.append(" ");
			b.append(code.toString());
		}
		b.append("\n");
		headers.forEach((name, list)->{
			list.forEach(value->{
				b.append(name + ": " + value);
				b.append("\n");
			});
		});
		b.append("\n");
		if (body != null) {
			b.append(body);
		} else if (!bodyParamters.toMap().isEmpty()) {
			b.append(bodyParamters.toMap().toString());
		}
		return b.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiRequest other = (ApiRequest) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (bodyParamters == null) {
			if (other.bodyParamters != null)
				return false;
		} else if (!bodyParamters.equals(other.bodyParamters))
			return false;
		if (code != other.code)
			return false;
		if (fullUrl == null) {
			if (other.fullUrl != null)
				return false;
		} else if (!fullUrl.equals(other.fullUrl))
			return false;
		if (headers == null) {
			if (other.headers != null)
				return false;
		} else if (!headers.equals(other.headers))
			return false;
		if (method != other.method)
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlParamters == null) {
			if (other.urlParamters != null)
				return false;
		} else if (!urlParamters.equals(other.urlParamters))
			return false;
		return true;
	}

}
