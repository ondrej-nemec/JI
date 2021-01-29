package acl.structures;

import java.util.LinkedList;
import java.util.List;

import acl.Action;

public class Rules {
	
	private final Action forUserId;
	private final List<String> owners;
	
	private final List<AccessRule> access;
	
	/**
	 * Create Rules for user
	 * @param forUser Action for user
	 * @param owners where user is allowed, empty mean no one, null means everyone
	 * @return
	 */
	public static Rules forUserWithOwner(Action forUser, List<String> owners) {
		return new Rules(forUser, owners, new LinkedList<>());
	}

	public static Rules forUserGroupsAndLevels(List<AccessRule> access) {
		return new Rules(Action.UNDEFINED, null, access);
	}
	
	public static Rules full(Action forUser, List<String> owners, List<AccessRule> access) {
		return new Rules(forUser, owners, access);
	}
	
	private Rules(Action forUserId, List<String> owners, List<AccessRule> access) {
		this.forUserId = forUserId;
		this.access = access;
		this.owners = owners;
	}

	public Action getForUserId() {
		return forUserId;
	}

	public List<AccessRule> getAccess() {
		return access;
	}

	public List<String> getOwners() {
		return owners;
	}
	
}
