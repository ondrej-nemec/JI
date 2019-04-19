package helper;

import exception.AccessDeniedException;
import interfaces.AclDestination;
import interfaces.AclRole;
import interfaces.AclRules;
import interfaces.AclUser;
import common.ILogger;

public class AuthorizationHelper {

	private AclRules rules;
	
	private final ILogger logger;
	
	public AuthorizationHelper(final AclRules rules, ILogger logger) {
		this.rules = rules;
		this.logger = logger;
	}
	
	public boolean isAllowed(final AclUser who, final AclDestination where, final Action what) {
		logger.debug("Access required: " + who + " -> " + where + " -> " + what);
		
		Status ruleUserId = rules.getRuleUserId(who.getId(), where.getId(), what);
		Status ruleUserRank = rules.getRuleUserRank(who.getRank(), where.getId(), what);
		Status ruleRoleId = Status.UNSPECIFIED;
		Status ruleRoleRank = Status.UNSPECIFIED;
		
		for (AclRole r : who.getRoles()) {
			ruleRoleId = resolveRolesStatus(
					ruleRoleId,
					rules.getRuleRoleId(r.getId(), where.getId(), what)
				);
			
			ruleRoleRank = resolveRolesStatus(
					ruleRoleRank,
					rules.getRuleRoleRank(r.getRank(), where.getId(), what)
				);
		}
				
		if (ruleUserId != Status.UNSPECIFIED)
			return resolveResult(ruleUserId);

		if (ruleUserRank != Status.UNSPECIFIED)
			return resolveResult(ruleUserRank);

		if (ruleRoleId != Status.UNSPECIFIED)
			return resolveResult(ruleRoleId);

		if (ruleRoleRank != Status.UNSPECIFIED)
			return resolveResult(ruleRoleRank);
		
		logger.warn("No access rule for: " + who + " -> " + where + " -> " + what); 
		return false;
	}
	
	private boolean resolveResult(final Status status) {
		if (status == Status.ALLOWED)
			return true;
		return false;
	}
	
	private Status resolveRolesStatus(final Status actual, final Status newS) {
		if (actual == Status.UNSPECIFIED)
			return newS;
		if (newS == Status.UNSPECIFIED)
			return actual;
		if (actual == Status.ALLOWED && newS == Status.DISALLOWED)
			return Status.ALLOWED;
		if (actual == Status.DISALLOWED && newS == Status.ALLOWED)
			return Status.ALLOWED;
		return actual;
	}
	
	public void throwIfIsNotAllowed(final AclUser who, final AclDestination where, final Action what) throws AccessDeniedException {
		if(!isAllowed(who, where, what))
			throw new AccessDeniedException(who, where, what);
	}
}
