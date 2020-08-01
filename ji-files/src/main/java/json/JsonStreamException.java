package json;

public class JsonStreamException extends Exception {

	private static final long serialVersionUID = 1L;

	public JsonStreamException(Throwable t) {
		super(t);
	}
	
	public JsonStreamException(String message) {
		super(message);
	}
	
}
