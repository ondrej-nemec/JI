package json;

import java.util.Map;

import json.providers.OutputStringProvider;

public class JsonWritter {
	
	public String write(Object json) {
		OutputStringProvider provider = new OutputStringProvider();
		OutputJsonStream stream = new OutputJsonStream(provider);
		try {
			write(stream, json);
		} catch (JsonStreamException e) {
			// ignored - no reason for it
		}
		return provider.getJson();
	}

	public void write(OutputJsonStream stream, Object json) throws JsonStreamException {
		writeObject(stream, json, null);
	}

	public void writeJson(OutputJsonStream stream, Map<String, Object> json) throws JsonStreamException {
		stream.startDocument();
		for (String key : json.keySet()) {
			writeObject(stream, json.get(key), key);
		}
		stream.endDocument();
	}
	
	/******************/
	
	private void write(OutputJsonStream stream, Map<String, Object> objects, String name) throws JsonStreamException {
		if (name == null) {
			stream.writeObjectStart();
		} else {
			stream.writeObjectStart(name);
		}
		for (Object key : objects.keySet()) {
			writeObject(stream, objects.get(key), key + "");
		}
		stream.writeObjectEnd();
	}
	
	private void write(OutputJsonStream stream, Iterable<Object> list, String name) throws JsonStreamException {
		if (name == null) {
			stream.writeListStart();
		} else {
			stream.writeListStart(name);
		}
		for (Object o : list) {
			writeObject(stream, o, null);
		}
		stream.writeListEnd();
	}
	
	
	@SuppressWarnings("unchecked")
	private void writeObject(OutputJsonStream stream, Object value, String name) throws JsonStreamException {
		if (value == null) {
			if (name == null) {
				stream.writeListValue(value);
			} else {
				stream.writeObjectValue(name, value);
			}
		} else if (value instanceof Jsonable) {
			writeObject(stream, ((Jsonable)value).toJson(), name);
		} else if (value instanceof Iterable) {
			write(stream, (Iterable<Object>)value, name);
		} else if (value instanceof Map) {
			write(stream, (Map<String, Object>)value, name);
		} else {
			if (name == null) {
				stream.writeListValue(value);
			} else {
				stream.writeObjectValue(name, value);
			}
		}	
	}
}
