package helper;

import exception.AccessDeniedException;
import exception.NotAllowedActionException;
import interfaces.AclDestination;
import interfaces.RulesDao;
import interfaces.AclUser;
import common.Logger;

public class AuthorizationHelper {

	private RulesDao rulesDao;
	
	private final Logger logger;
	
	public AuthorizationHelper(final RulesDao rulesDao, Logger logger) {
		this.rulesDao = rulesDao;
		this.logger = logger;
	}
	
	public void throwIfIsNotAllowed(final AclUser who, final AclDestination where, final Action what) {
		if(!isAllowed(who, where, what)) {
			throw new AccessDeniedException(who, where, what);
		}
	}
	
	public boolean isAllowed(final AclUser who, final AclDestination where, final Action what) {
		logger.debug("Access required: " + who + " -> " + where + " -> " + what);
		
		if (what == Action.FORBIDDEN || what == Action.UNDEFINED) {
			throw new NotAllowedActionException(what);
		}
		
		Rules rules = rulesDao.getRulesForUser(who, where);
		
		Action userId = rules.getForUserId();		
		if (userId != Action.UNDEFINED) {
			return isAllowed(userId, what);
		}
		
		Action userRank = rules.getForUserRank();
		if (userRank != Action.UNDEFINED) {
			return isAllowed(userRank, what);
		}
		
		Action roleId = Action.UNDEFINED;
		for (Action actualRole : rules.getForRoleIds()) {
			roleId = selectRole(roleId, actualRole);
		}
		if (roleId != Action.UNDEFINED) {
			return isAllowed(roleId, what);
		}
		
		logger.warn("No access rule for: " + who + " -> " + where + " -> " + what); 
		return false;
	}

	private Action selectRole(Action origin, Action actual) {
		if (origin.ordinal() >= actual.ordinal()) {
			return origin;
		}
		return actual;
	}
	
	private boolean isAllowed(Action finded, Action required) {
		if (finded != Action.FORBIDDEN) {
			return finded.ordinal() >= required.ordinal();
		}
		return false;
	}
	
}
