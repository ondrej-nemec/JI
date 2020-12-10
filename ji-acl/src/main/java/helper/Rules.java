package helper;

import java.util.LinkedList;
import java.util.List;

public class Rules {
	
	private final Action forUserId;
	
	private final List<AccessRule> access;
	
	public Rules(Action forUser) {
		this(forUser, new LinkedList<>());
	}

	public Rules(List<AccessRule> access) {
		this(Action.UNDEFINED, access);
	}

	public Rules(Action forUserId, List<AccessRule> access) {
		this.forUserId = forUserId;
		this.access = access;
	}

	public Action getForUserId() {
		return forUserId;
	}

	public List<AccessRule> getAccess() {
		return access;
	}
	
}
