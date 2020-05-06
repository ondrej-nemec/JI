package json.providers;

import json.JsonStreamException;

public interface Provider {
	
	public char getNext() throws JsonStreamException;
	
}
