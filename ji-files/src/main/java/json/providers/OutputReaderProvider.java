package json.providers;

import java.io.IOException;
import java.io.Writer;

import json.JsonStreamException;

public class OutputReaderProvider implements OutputProvider {

	private final Writer writer;
	
	public OutputReaderProvider(Writer writer) {
		this.writer = writer;
	}
	
	@Override
	public void write(String json) throws JsonStreamException {
		try {
			writer.write(json);
		} catch (IOException e) {
			throw new JsonStreamException(e);
		}
	}

	@Override
	public void close() throws JsonStreamException {
		// TODO Auto-generated method stub
	}

}
