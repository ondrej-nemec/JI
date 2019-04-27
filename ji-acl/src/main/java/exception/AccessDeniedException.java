package exception;

import helper.Action;
import interfaces.AclDestination;
import interfaces.AclUser;

public class AccessDeniedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {
		super();
	}
	
	public AccessDeniedException(AclUser user, AclDestination destination, Action action) {
		super(user + ":" + destination + ":" + action);
	}
}
