package ji.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XmlObject {

	private final String name;
	
	private final StringBuilder value;
	private final Map<String, String> attributes;
	private final List<XmlObject> references;

	public XmlObject(String name) {
		this.name = name;
		this.attributes = new HashMap<>();
		this.references = new ArrayList<>();
		this.value = new StringBuilder();
	}
	
	public String getName() {
		return name;
	}
	
	public Optional<String> getValue() {
		if (value.toString().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(value.toString());
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public List<XmlObject> getReferences() {
		return references;
	}
	
	public XmlObject addValue(String value) {
		this.value.append(value);
		return this;
	}
	
	public XmlObject addAtribute(String key, String value) {
		this.attributes.put(key, value);
		return this;
	}
	
	public XmlObject addReference(XmlObject reference) {
		this.references.add(reference);
		return this;
	}
	
}
