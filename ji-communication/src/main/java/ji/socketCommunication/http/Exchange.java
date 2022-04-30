package ji.socketCommunication.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ji.common.structures.DictionaryValue;
import ji.socketCommunication.http.parsers.BodyType;

public abstract class Exchange {
	
	private final Map<String, List<Object>> headers = new HashMap<>();	
	private Object body;
	private BodyType type = BodyType.EMPTY;
	
	public abstract String getFirstLine();
	
	/**** headers section *****/
	
	public void addHeader(String name, Object value) {
		if (!headers.containsKey(name)) {
			headers.put(name, new LinkedList<>());
		}
		headers.get(name).add(value);
	}
	
	public Map<String, List<Object>> getHeaders() {
		return headers;
	}
	
	public void setHeaders(Map<String, List<Object>> headers) {
		this.headers.putAll(headers);
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
	
	public boolean containsHeader(String name) {
		return headers.containsKey(name) && headers.get(name).size() > 0;
	}
	
	public <T> T getHeader(String name, Class<T> clazz) {
		return new DictionaryValue(getHeader(name)).getValue(clazz);
	}
	
	/**** body section ***/
	/*
	public void setBody(Map<String, Object> parameters) {
		
	}
	*/
	
	public Map<String, Object> getBodyFormData() {
		return getBodyUrlEncoded();
	}
	
	public Map<String, Object> getBodyJson() {
		return getBodyUrlEncoded();
	}
	
	public Map<String, Object> getBodyUrlEncoded() {
		return new DictionaryValue(body).getMap();
	}
	
	public byte[] getBodyBinary() {
		
		if (body instanceof byte[]) {
			return (byte[])body;
		}
		return body.toString().getBytes();
	}
	
	public String getBodyText() {
		if (body instanceof byte[]) {
			return new String((byte[])body);
		}
		return body.toString();
	}
	
	public Object getBody() {
		return body;
	}
	
	public BodyType getBodyType() {
		return type;
	}

	// for full request binary or string
	public void setBodyPlainOrBinary(byte[] body) {
		this.body = body;
		this.type = BodyType.PLAIN_TEXT_OR_BINARY;
	}

	public void setBodyText(String body) {
		this.body = body;
		this.type = BodyType.PLAIN_TEXT_OR_BINARY;
	}
	
	public void setBodyBinary(byte[] body) {
		this.body = body;
		this.type = BodyType.PLAIN_TEXT_OR_BINARY;
	}

	public void setBodyJson(Map<String, Object> body) {
		this.body = body;
		this.type = BodyType.JSON;
	}
	
	public void setBodyFormData(Map<String, Object> body) {
		this.body = body;
		this.type = BodyType.FORM_DATA;
	}
	
	public void setBodyUrlEncoded(Map<String, Object> body) {
		this.body = body;
		this.type = BodyType.URLENCODED_DATA;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getFirstLine());
		b.append("\n");
		headers.forEach((name, list)->{
			list.forEach((value)->{
				b.append(String.format("%s: %s\n", name, value));
			});
		});
		b.append("\n");
		b.append(body);
		return b.toString();
	}
	
}
