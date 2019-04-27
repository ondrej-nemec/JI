package helper;

import exception.AccessDeniedException;
import exception.NotAllowedActionException;
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
	
	public void throwIfIsNotAllowed(final AclUser who, final AclDestination where, final Action what) {
		if(!isAllowed(who, where, what))
			throw new AccessDeniedException(who, where, what);
	}
	
	public boolean isAllowed(final AclUser who, final AclDestination where, final Action what) {
		logger.debug("Access required: " + who + " -> " + where + " -> " + what);
		
		if (what == Action.FORBIDDEN || what == Action.UNDEFINED)
			throw new NotAllowedActionException(what);
		
		Action userId = rules.getActionForUserId(who.getId(), where.getId());
		if (userId != Action.UNDEFINED)
			return isAllowed(userId, what);
		
		Action userRank = rules.getActionForUserRank(who.getRank(), where.getId());
		if (userRank != Action.UNDEFINED)
			return isAllowed(userRank, what);		
		
		Action roleId = Action.UNDEFINED;
		for (AclRole role : who.getRoles()) {
			roleId = selectRole(
						roleId,
						rules.getActionForRoleId(role.getId(), where.getId())
					);
		}
		if (roleId != Action.UNDEFINED)
			return isAllowed(roleId, what);		
		
		logger.warn("No access rule for: " + who + " -> " + where + " -> " + what); 
		return false;
	}

	private Action selectRole(Action origin, Action actual) {
		if (origin.ordinal() >= actual.ordinal())
			return origin;
		return actual;
	}
	
	private boolean isAllowed(Action finded, Action required) {
		if (finded == Action.FORBIDDEN)
			return false;
		if (finded.ordinal() >= required.ordinal())
			return true;
		return false;
	}
	
}
