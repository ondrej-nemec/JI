package helper;

public class AccessRule {

	private final Integer rank;
	private final Action action;
	
	public AccessRule(Integer rank, Action action) {
		this.rank = rank;
		this.action = action;
	}

	public Integer getRank() {
		return rank;
	}

	public Action getAction() {
		return action;
	}
	
}
