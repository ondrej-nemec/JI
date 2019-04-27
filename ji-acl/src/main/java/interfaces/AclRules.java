package interfaces;

import helper.Action;

public interface AclRules {
	
	default Action getActionForUserId(String userId, String destinationId) {
		return Action.UNDEFINED;
	}
	
	default Action getActionForUserRank(int userRank, String destinationId) {
		return Action.UNDEFINED;
	}
	
	default Action getActionForRoleId(String roleId, String destinationId) {
		return Action.UNDEFINED;
	}
}
