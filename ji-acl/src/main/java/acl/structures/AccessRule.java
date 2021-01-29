package acl.structures;

import java.util.List;

import acl.Action;

public class AccessRule {

	private final Integer rank;
	private final Action action;
	
	private final List<String> owners;
	
	public static AccessRule withoutLevel(Action action, List<String> owners) {
		return new AccessRule(null, action, owners);
	}
	
	public static AccessRule withLevel(int rank, Action action, List<String> owners) {
		return new AccessRule(rank, action, owners);
	}
	
	private AccessRule(Integer rank, Action action, List<String> owners) {
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

	public List<String> getOwners() {
		return owners;
	}
	
}
