package ji.socketCommunication.http.parsers;

import java.io.IOException;

public class ParsingException extends IOException {
	
	private static final long serialVersionUID = 1L;

	public ParsingException(String message) {
		super(message);
	}
	
}
