package acl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import acl.exception.AccessDeniedException;
import acl.exception.NotAllowedActionException;
import acl.structures.AccessRule;
import acl.structures.AclDestination;
import acl.structures.AclUser;
import acl.structures.Rules;
import common.Logger;

public class AuthorizationHelper {

	private Permissions permissions;
	
	private final Logger logger;
	
	public AuthorizationHelper(final Permissions permissions, Logger logger) {
		this.permissions = permissions;
		this.logger = logger;
	}
	
	public void authenticate(AclUser who, AclDestination where, Action what) {
		if(!isAllowed(who, where, what)) {
			throw new AccessDeniedException(who, where, what);
		}
	}
	
	public void authenticate(AclUser who, AclDestination where, Action what, Object owner) {
		if(!isAllowed(who, where, what, owner)) {
			throw new AccessDeniedException(who, where, what);
		}
	}
	
	public boolean isAllowed(AclUser who, AclDestination where, Action what) {
		return isAllowed(who, where, what, null);
	}
	
	public boolean isAllowed(AclUser who, AclDestination where, Action what, Object owner) {
		logger.debug("Access required: " + who + " -> " + where + " -> " + what + " (" + owner + ")");
		
		if (what == Action.FORBIDDEN || what == Action.UNDEFINED) {
			throw new NotAllowedActionException(what);
		}
		
		Rules rules = permissions.getRulesForUserAndGroups(who, where);
		if (rules == null) {
			logger.warn("No access rule found for: " + who + " -> " + where + " -> " + what + " (" + owner + ")"); 
			return false;
		}
		if (rules.getForUserId() != Action.UNDEFINED) {
			boolean owned = rules.getOwners() != null ? rules.getOwners().contains(owner) : true;
			return isAllowed(rules.getForUserId(), what) && owned;
		}
		
		Action action = Action.UNDEFINED;
		if (rules.getAccess() != null) {
			for (AccessRule rule : rules.getAccess()) {
				if (rule.getRank() == null || rule.getRank() <= who.getRank()) {
					boolean owned = rule.getOwners() != null ? rule.getOwners().contains(owner) : true;
					action = owned ? selectRole(action, rule.getAction()) : Action.UNDEFINED;
				}
			}
		}
		
		if (action != Action.UNDEFINED) {
			return isAllowed(action, what);
		}
		
		logger.warn("No access rule for: " + who + " -> " + where + " -> " + what + " (" + owner + ")"); 
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
	
	/**
	 * 
	 * @param who
	 * @param where
	 * @param what
	 * @return list of allowed user ids, empty means allowed to destination, but not for users, null means not allowed
	 */
	public Collection<Object> getAllowed(AclUser who, AclDestination where, Action what) {
		try {
			return allowed(who, where, what);
		} catch (AccessDeniedException e) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param who
	 * @param where
	 * @param what
	 * @throws AccessDeniedException
	 * @return list of allowed user ids, empty means allowed to destination, but not for users
	 */
	public Collection<Object> allowed(AclUser who, AclDestination where, Action what) {		
		logger.debug("Access required: " + who + " -> " + where + " -> " + what);
		
		if (what == Action.FORBIDDEN || what == Action.UNDEFINED) {
			throw new NotAllowedActionException(what);
		}
		
		Rules rules = permissions.getRulesForUserAndGroups(who, where);
		if (rules == null) {
			logger.warn("No access rule found for: " + who + " -> " + where + " -> " + what); 
			throw new NotAllowedActionException("No rule found");
		}
		Set<Object> allowedIds = new HashSet<>();
		Set<Object> forbidden = new HashSet<>();
		Action action = Action.UNDEFINED;
		if (rules.getAccess() != null) {
			for (AccessRule rule : rules.getAccess()) {
				if (rule.getRank() == null || rule.getRank() <= who.getRank()) {
					if (rule.getOwners() == null) {
						throw new NotAllowedActionException("Owners for Role Rule are not defined");
					}
					if (rule.getAction() == Action.FORBIDDEN) {
						forbidden.addAll(rule.getOwners());
					} else if (isAllowed(rule.getAction(), what)) {
						action = selectRole(action, rule.getAction());
						allowedIds.addAll(rule.getOwners());
					}
				}
			}
		}
		allowedIds.removeAll(forbidden);
		if (rules.getForUserId() != Action.UNDEFINED) {
			if (rules.getOwners() == null) {
				throw new NotAllowedActionException("Owners for User Rule are not defined");
			}
			if (rules.getForUserId() == Action.FORBIDDEN) {
				allowedIds.removeAll(rules.getOwners());
			} else if (isAllowed(rules.getForUserId(), what)) {
				allowedIds.addAll(rules.getOwners());
				action = rules.getForUserId();
			}
		}
		if (action == Action.UNDEFINED) {
			logger.warn("No access rule for: " + who + " -> " + where + " -> " + what);
			throw new AccessDeniedException(who, where, what);
		}
		if (action == Action.FORBIDDEN) {
			throw new AccessDeniedException(who, where, what);
		}
		return allowedIds;
	}
	
}
