package interfaces;

import helper.Rules;

public interface RulesDao<U, R> {
	
	/**
	 * Select rules for given user and his/her groups.
	 * If rule for user id (user id in AclUser), then used
	 * Otherwise look for highest rule for group-rank pair (group or rank can be null)
	 * All rules must be related to user and groups (or null groups), rank is optional
	 * @param user
	 * @param domain
	 * @return
	 */
	Rules getRulesForUserAndGroups(AclUser<U, R> user, AclDestination domain);
	
}
