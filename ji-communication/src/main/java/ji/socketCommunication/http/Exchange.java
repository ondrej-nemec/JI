package ji.socketCommunication.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ji.common.structures.DictionaryValue;
import ji.socketCommunication.http.parsers.BodyType;

public abstract class Exchange {
	
	private final Map<String, List<Object>> headers = new HashMap<>();
	
	private BodyType type = BodyType.BASIC;
	private RequestParameters parameters;
	private byte[] body;
	private WebSocket websocket;
		
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
	
	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public RequestParameters getBodyInParameters() {
		return parameters;
	}
	
	public WebSocket getBodyWebsocket() {
		return websocket;
	}
	
	public void setBodyFormData(RequestParameters body) {
		this.parameters = body;
		this.type = BodyType.FORM_DATA;
	}
	
	public void setBodyUrlencoded(RequestParameters body) {
		this.parameters = body;
		this.type = BodyType.URLENCODED_DATA;
	}
	
	public void setBodyWebsocket(WebSocket webSocket) {
		this.websocket = webSocket;
		this.type = BodyType.WEBSOCKET;
	}
	
	public BodyType getType() {
		return type;
	}

	/************/
	
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
		
		if (body == null) {
			b.append(parameters);
		} else {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				os.write(body);
			} catch (IOException e) {}
			b.append(new String(os.toByteArray()));
		}
		
		
		
		return b.toString();
	}
	
}
