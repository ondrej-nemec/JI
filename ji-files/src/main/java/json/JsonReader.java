package json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import common.exceptions.LogicException;
import common.functions.Mapper;
import json.event.Event;
import json.event.EventType;
import json.providers.InputStringProvider;

public class JsonReader {
	
	public <T> T read(String json, Class<T> clazz) throws JsonStreamException {
		try {
			return Mapper.get().parse(clazz, read(json));
		} catch (Exception e) {
			throw new JsonStreamException(e);
		}
	}
	
	public <T> T read(InputJsonStream stream, Class<T> clazz) throws JsonStreamException {
		try {
			return Mapper.get().parse(clazz, read(stream));
		} catch (Exception e) {
			throw new JsonStreamException(e);
		}
	}
	
	public Object read(String json) throws JsonStreamException {
		InputStringProvider provider = new InputStringProvider(json);
		InputJsonStream stream = new InputJsonStream(provider);
		return read(stream);
	}
	
	public Object read(InputJsonStream stream) throws JsonStreamException {
		Event event = stream.next();
		if (event.getType() == EventType.DOCUMENT_START) {
			Map<String, Object> json = new HashMap<>();
			while((event = stream.next()).getType() != EventType.DOCUMENT_END) {
				switch (event.getType()) {
					case OBJECT_ITEM: json.put(event.getName(), event.getValue().get()); break;
					case OBJECT_START: json.put(event.getName(), readMap(stream)); break;
					case LIST_START: json.put(event.getName(), readList(stream)); break;
					default: throw new LogicException("Unsupported event type " + event.getType());
				}
			}
			return json;
		} else if (event.getType() == EventType.LIST_START) {
			List<Object> list = new LinkedList<>();
			event = stream.next();
			while(event.getType() != EventType.LIST_END && event.getType() != EventType.DOCUMENT_END) {
				switch (event.getType()) {
					case LIST_ITEM: list.add(event.getValue().get()); break;
					case OBJECT_START: list.add(readMap(stream)); break;
					case LIST_START: list.add(readList(stream)); break;
					default: throw new LogicException("Unsupported event type " + event.getType());
				}
				event = stream.next();
			}
			return list;
		} else {
			throw new LogicException("Unsupported event type " + event.getType());
		}
	}
	
	/***************************/
	
	private Map<String, Object> readMap(InputJsonStream stream) throws JsonStreamException {
		Map<String, Object> map = new HashMap<>();
		Event event = stream.next();
		while(event.getType() != EventType.OBJECT_END && event.getType() != EventType.DOCUMENT_END) {
			switch (event.getType()) {
				case DOCUMENT_START:
				case OBJECT_ITEM: map.put(event.getName(), event.getValue().get()); break;
				case OBJECT_START: map.put(event.getName(), readMap(stream)); break;
				case LIST_START: map.put(event.getName(), readList(stream)); break;
				default: throw new LogicException("Unsupported event type " + event.getType());
			}
			event = stream.next();
		}
		return map;
	}
	
	private List<Object> readList(InputJsonStream stream) throws JsonStreamException {
		List<Object> list = new LinkedList<>();
		Event event;
		while((event = stream.next()).getType() != EventType.LIST_END) {
			switch (event.getType()) {
				case LIST_ITEM: list.add(event.getValue().get()); break;
				case OBJECT_START: list.add(readMap(stream)); break;
				case LIST_START: list.add(readList(stream)); break;
				default: throw new LogicException("Unsupported event type " + event.getType());
			}
		}
		return list;
	}
	
}
