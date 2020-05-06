package json.event;

public class Event {
	
	private EventType type;
	
	private String name;
	
	private Value value;

	public Event(EventType type, String name, Value value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public EventType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Value getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s (%s)", name, value, type);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event)) {
			return false;
		}
		Event e = (Event)obj;
		if (e.type != type) {
			return false;
		}
		if (!name.equals(e.name)) {
			return false;
		}
		if (!value.equals(e.value)) {
			return false;
		}
		return true;
	}
	
}
