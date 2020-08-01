package json.providers;

import json.JsonStreamException;

public interface OutputProvider {

	void write(String json) throws JsonStreamException;
	
	void close() throws JsonStreamException;
	
}
