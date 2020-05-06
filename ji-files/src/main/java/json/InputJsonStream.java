package json;

import json.event.Event;
import json.event.EventType;
import json.event.Value;
import json.providers.Provider;
import json.providers.StringProvider;

public class InputJsonStream {
	
	public static InputJsonStream fromString(String json) {
		return new InputJsonStream(new StringProvider(json));
	}
	
	private final static char CHAR_DEFAULT = '\u0000';
	
	private final Provider provider;
	
	private char actualCache = CHAR_DEFAULT;
	private char previousChar = CHAR_DEFAULT;
	
	public InputJsonStream(Provider provider) {
		this.provider = provider;
	}
	
	public Event next() throws JsonStreamException {
		String name = "";
		String value = "";
		boolean isInQuots = false;
		boolean isWaitingForValue = false;
		boolean isValueQuoted = false;
		
		char actual = (actualCache == CHAR_DEFAULT) ? provider.getNext() : actualCache;
		while(actual != (char)-1) {
			/***** if char is in quotes *******/
			if (actual != '"' && isInQuots) {
				if (isWaitingForValue) {
					value += actual;
				} else {
					name += actual;
				}
				//////
					previousChar = actual;
					actual = provider.getNext();
				//////
				continue;
			}
			/******** whitespace chars outside of quotes are ignored **********/
			if (Character.isWhitespace(actual)) {
				//////
					previousChar = actual;
					actual = provider.getNext();
				//////
				continue;
			}
			/*********  cache check  *************/
			if (actualCache != CHAR_DEFAULT) {
				char cache = actualCache;
				actualCache = CHAR_DEFAULT;
				switch (cache) {
					case '}':
						return new Event(EventType.OBJECT_END, name, new Value(value, isValueQuoted));
					case ']':
						return new Event(EventType.LIST_END, name, new Value(value, isValueQuoted));
				}
				continue;
			}
			/******** chars with special meaning **************/
			switch (actual) {
				case '"':
					if (!isInQuots) {
						isInQuots = true;
						isValueQuoted = isWaitingForValue; // IMPORTANT
					} else if (previousChar != '\\') {
						isInQuots = false;
					} else if (isWaitingForValue) {
						value += actual;
					} else {
						name += actual;
					}
					break;
				case '{': return new Event(EventType.OBJECT_START, name, new Value(value, isValueQuoted));
				case '}':
					actualCache = actual;
					if (!name.isEmpty() && (isValueQuoted || !value.isEmpty())) {
						return new Event(EventType.OBJECT_ITEM, name, new Value(value, isValueQuoted));
					}
					break;
				case '[': return new Event(EventType.LIST_START, name, new Value(value, isValueQuoted));
				case ']':
					actualCache = actual;
					if (!name.isEmpty()) { // list has not name, value is in name
						return new Event(EventType.LIST_ITEM, value, new Value(name, isValueQuoted));
					}
					break;
				case ',':
					if (isWaitingForValue) {
						return new Event(EventType.OBJECT_ITEM, name, new Value(value, isValueQuoted));
					}
					if (!value.isEmpty()) {
						return new Event(EventType.LIST_ITEM, name, new Value(value, isValueQuoted));
					}
					break;
				case ':': isWaitingForValue = true; break;
				/********* chars outside of quotes without special meaning **********/
				default:
					value += actual;
			}		

			//////
				previousChar = actual;
				actual = (actualCache == CHAR_DEFAULT) ? provider.getNext() : actualCache;
			//////
		}
		
		return new Event(EventType.DOKUMENT_END, name, new Value(value, isValueQuoted));
	}
	
}
