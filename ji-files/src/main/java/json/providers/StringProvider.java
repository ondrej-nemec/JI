package json.providers;

import json.JsonStreamException;

public class StringProvider implements Provider {

	private String json;
	
	private int index = 0;
	
	public StringProvider(String json) {
		this.json = json;
	}
	
	@Override
	public char getNext() throws JsonStreamException {
		if (index < json.length()) {
			return json.charAt(index++);
		}
		return (char)-1;
	}
	
}
