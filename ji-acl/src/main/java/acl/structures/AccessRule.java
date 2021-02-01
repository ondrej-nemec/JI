package acl.structures;

import java.util.List;

import acl.Action;

public class AccessRule {

	private final Integer rank;
	private final Action action;
	
	private final List<Object> owners;
	
	public static AccessRule withoutLevel(Action action, List<Object> owners) {
		return new AccessRule(null, action, owners);
	}
	
	public static AccessRule withLevel(int rank, Action action, List<Object> owners) {
		return new AccessRule(rank, action, owners);
	}
	
	private AccessRule(Integer rank, Action action, List<Object> owners) {
		this.rank = rank;
		this.action = action;
		this.owners = owners;
	}

	public Integer getRank() {
		return rank;
	}

	public Action getAction() {
		return action;
	}

	public List<Object> getOwners() {
		return owners;
	}
	
}
