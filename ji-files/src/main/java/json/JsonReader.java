package json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import common.exceptions.LogicException;
import json.event.Event;
import json.event.EventType;
import json.providers.InputStringProvider;

public class JsonReader {

	public Map<String, Object> read(String json) throws JsonStreamException {
		InputStringProvider provider = new InputStringProvider(json);
		InputJsonStream stream = new InputJsonStream(provider);
		return read(stream);
	}
	
	
	public Map<String, Object> read(InputJsonStream stream) throws JsonStreamException {
		Map<String, Object> json = new HashMap<>();
		Event event = stream.next(); // document start
		while((event = stream.next()).getType() != EventType.DOCUMENT_END) {
			switch (event.getType()) {
				case OBJECT_ITEM: json.put(event.getName(), event.getValue().get()); break;
				case OBJECT_START: json.put(event.getName(), readObject(stream)); break;
				case LIST_START: json.put(event.getName(), readList(stream)); break;
				default: throw new LogicException("Unsupported event type " + event.getType());
			}
		}
		return json;
	}
	
	
	private Object readObject(InputJsonStream stream) throws JsonStreamException {
		Map<String, Object> map = new HashMap<>();
		Event event;
		while((event = stream.next()).getType() != EventType.OBJECT_END) {
			switch (event.getType()) {
				case OBJECT_ITEM: map.put(event.getName(), event.getValue().get()); break;
				case OBJECT_START: map.put(event.getName(), readObject(stream)); break;
				case LIST_START: map.put(event.getName(), readList(stream)); break;
				default: throw new LogicException("Unsupported event type " + event.getType());
			}
		}
		return map;
	}
	
	private Object readList(InputJsonStream stream) throws JsonStreamException {
		List<Object> list = new LinkedList<>();
		Event event;
		while((event = stream.next()).getType() != EventType.LIST_END) {
			switch (event.getType()) {
				case LIST_ITEM: list.add(event.getValue().get()); break;
				case OBJECT_START: list.add(readObject(stream)); break;
				case LIST_START: list.add(readList(stream)); break;
				default: throw new LogicException("Unsupported event type " + event.getType());
			}
		}
		return list;
	}
	
}
