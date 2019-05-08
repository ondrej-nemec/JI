package helper;

import java.util.List;

public class Rules {
	
	private final Action forUserId;
	
	private final Action forUserRank;
	
	private final List<Action> forRoleIds;

	public Rules(final Action forUserId, final Action forUserRank, final List<Action> forRoleIds) {
		this.forUserId = forUserId;
		this.forUserRank = forUserRank;
		this.forRoleIds = forRoleIds;
	}

	public Action getForUserId() {
		return forUserId;
	}

	public Action getForUserRank() {
		return forUserRank;
	}

	public List<Action> getForRoleIds() {
		return forRoleIds;
	}
	
}
