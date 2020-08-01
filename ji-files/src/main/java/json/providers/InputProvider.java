package json.providers;

import json.JsonStreamException;

public interface InputProvider {
	
	public char getNext() throws JsonStreamException;
	
	public void close() throws JsonStreamException;
	
}
