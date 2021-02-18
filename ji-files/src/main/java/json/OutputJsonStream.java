package json;

import java.util.LinkedList;

import json.event.EventType;
import json.providers.OutputProvider;

public class OutputJsonStream {

	private final OutputProvider provider;
	
	private final LinkedList<Boolean> parent = new LinkedList<>();
	
	private final boolean formated;
	
	public OutputJsonStream(OutputProvider provider) {
		this(provider, false);
	}
	
	public OutputJsonStream(OutputProvider provider, boolean formated) {
		this.provider = provider;
		this.formated = formated;
	}
	
	public void startDocument() throws JsonStreamException {
		provider.write("{");
		parent.add(true);
	}
	
	public void endDocument() throws JsonStreamException {
		parent.removeLast();
		provider.write(getFormat(true) + "}");
		provider.close();
	}
	
	public void writeObjectValue(String name, Object value) throws JsonStreamException {
		boolean isFirst = checkFirst();
		provider.write(getFormat(isFirst) + String.format("\"%s\":%s", name, (formated ? " " : "") + getValue(value)));
	}

	public void writeObjectStart() throws JsonStreamException {
		boolean isFirst = checkFirst(EventType.OBJECT_START);
		provider.write(getFormat(isFirst) + "{");
	}
	
	public void writeObjectStart(String name) throws JsonStreamException {
		boolean isFirst = checkFirst(EventType.OBJECT_START);
		provider.write(getFormat(isFirst) + String.format("\"%s\":%s{", name, (formated ? " " : "")));
	}
	
	public void writeObjectEnd() throws JsonStreamException {
		provider.write(getFormat(true) + "}");
		parent.removeLast();
	}
	
	public void writeListValue(Object value) throws JsonStreamException {
		boolean isFirst = checkFirst();
		provider.write(getFormat(isFirst) + String.format("%s", getValue(value)));
	}
	
	public void writeListStart() throws JsonStreamException {
		boolean isFirst = checkFirst(EventType.LIST_START);
		provider.write(getFormat(isFirst) + "[");
	}
	
	public void writeListStart(String name) throws JsonStreamException {
		boolean isFirst = checkFirst(EventType.LIST_START);
		provider.write(getFormat(isFirst) + String.format("\"%s\":%s[", name, (formated ? " " : "")));
	}
	
	public void writeListEnd() throws JsonStreamException {
		provider.write(getFormat(true) + "]");
		parent.removeLast();
	}
	
	private String getValue(Object value) {
		if (value == null) {
			return "null";
		}
		if (value instanceof Boolean) {
			return value.toString();
		}
		if (value instanceof Number) {
			return value.toString();
		}
		return String.format("\"%s\"", value);
	}
	
	private boolean checkFirst(EventType child) {
		if (parent.size() == 0) {
			parent.add(true);
			return true;
		}
		boolean isFirst = parent.getLast();
		if (isFirst) {
			parent.removeLast();
			parent.add(false);
		}
		parent.add(true);
		return isFirst;
	}
	
	private boolean checkFirst() {
		boolean isFirst = parent.getLast();
		if (isFirst) {
			parent.removeLast();
			parent.add(false);
		}
		return isFirst;
	}
	
	private String getFormat(boolean isFirst) {
		StringBuilder pre = new StringBuilder((isFirst ? "" : ","));
		if (formated) {
			pre.append("\n");
			parent.forEach((item)->{
				pre.append("  ");
			});
		}
		return pre.toString();
	}
	
}
