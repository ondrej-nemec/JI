package json;

import java.util.List;
import java.util.Map;

import json.providers.OutputStringProvider;

public class OutputJsonWritter {
	
	public String write(Map<String, Object> json) throws JsonStreamException {
		OutputStringProvider provider = new OutputStringProvider();
		OutputJsonStream stream = new OutputJsonStream(provider);
		write(stream, json);
		return provider.getJson();
	}

	public void write(OutputJsonStream stream, Map<String, Object> json) throws JsonStreamException {
		stream.startDocument();
		for (String key : json.keySet()) {
			writeObject(stream, json.get(key), key);
		}
		stream.endDocument();
	}
	
	private void write(OutputJsonStream stream, Map<String, Object> objects, String name) throws JsonStreamException {
		if (name == null) {
			stream.writeObjectStart();
		} else {
			stream.writeObjectStart(name);
		}
		for (String key : objects.keySet()) {
			writeObject(stream, objects.get(key), key);
		}
		stream.writeObjectEnd();
	}
	
	private void write(OutputJsonStream stream, List<Object> list, String name) throws JsonStreamException {
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
		} else if (value instanceof List) {
			write(stream, (List<Object>)value, name);
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
