package utils.security;

public class CryptException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CryptException(Exception exception) {
		super(exception);
	}

}
