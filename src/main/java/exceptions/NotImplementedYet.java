package exceptions;

public class NotImplementedYet extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotImplementedYet() {
		super("This method is not implemented yet");
	}

}
