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
		
		Rules rules = rulesDao.getRulesForUserAndGroups(who, where);
		if (rules.getForUserId() != Action.UNDEFINED) {
			return isAllowed(rules.getForUserId(), what);
		}
		
		Action action = Action.UNDEFINED;
		for (AccessRule rule : rules.getAccess()) {
			if (rule.getRank() == null || rule.getRank() <= who.getRank()) {
				action = selectRole(action, rule.getAction());
			}
		}
		if (action != Action.UNDEFINED) {
			return isAllowed(action, what);
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
