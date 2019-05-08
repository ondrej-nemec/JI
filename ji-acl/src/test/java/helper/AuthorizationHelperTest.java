package helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import exception.AccessDeniedException;
import exception.NotAllowedActionException;
import interfaces.AclDestination;
import interfaces.AclRole;
import interfaces.RulesDao;
import interfaces.AclUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import common.Logger;

@RunWith(JUnitParamsRunner.class)
public class AuthorizationHelperTest {
	
	@Test
	@Parameters
	public void testIsAllowedReturnFalseIfNoRulesGiven(Action action) {
		AclDestination domain = getDestination("undefined");
		AclRole role = getRole("role");
		AclUser user = getUser("user", 10, role);
		
		RulesDao mock = mock(RulesDao.class);
		
		when(mock.getRulesForUser(user, domain)).thenReturn(
			new Rules(Action.UNDEFINED, Action.UNDEFINED, new LinkedList<>())
		);
		
		AuthorizationHelper helper = getHelper(mock);
		assertEquals(
				false,
				helper.isAllowed(user, domain, action)
		);
	}
	
	public Object[] parametersForTestIsAllowedReturnFalseIfNoRulesGiven() {
		return new Object[] {
				new Object[]{
						Action.READ,	
				},
				new Object[]{
						Action.UPDATE,
				},
				new Object[]{
						Action.CREATE,	
				},
				new Object[]{
						Action.DELETE
				},
				new Object[]{
						Action.ADMIN	
				}
		};
	}
	
	@Test(expected=NotAllowedActionException.class)
	@Parameters
	public void testIsAllowedThrowsIfNotAllowedActionIsGiven(Action act) {
		AuthorizationHelper helper = getHelper(null);
		helper.isAllowed(null, null, act);
	}
	
	public Object[] parametersForTestIsAllowedThrowsIfNotAllowedActionIsGiven() {
		return new Object[] {
				new Object[]{Action.UNDEFINED},
				new Object[]{Action.FORBIDDEN},
		};
	}

	@Test
	@Parameters
	public void testIsAllowedWithUserIdRules(Action action, boolean isAllowed) {
		AclRole role = getRole("role");
		AclUser disallowed = getUser("disalowed", 10, role);
		AclUser allowed = getUser("allowed", 10, role);
		AclUser forbidden = getUser("forbidden", 10, role);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(allowed, destination)).thenReturn(
			new Rules(Action.CREATE, Action.UNDEFINED, new LinkedList<>())
		);
		
		when(mock.getRulesForUser(forbidden, destination)).thenReturn(
			new Rules(Action.FORBIDDEN, Action.UNDEFINED, new LinkedList<>())
		);
		
		when(mock.getRulesForUser(disallowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Action.UNDEFINED, new LinkedList<>())
		);
		
		AuthorizationHelper helper = getHelper(mock);
		
		assertFalse(helper.isAllowed(disallowed, destination, action));
		assertFalse(helper.isAllowed(forbidden, destination, action));
		assertEquals(isAllowed, helper.isAllowed(allowed, destination, action));		
	}
	
	public Object[] parametersForTestIsAllowedWithUserIdRules() {
		return new Object[] {
				new Object[]{
						Action.READ, true	
				},
				new Object[]{
						Action.UPDATE, true
				},
				new Object[]{
						Action.CREATE, true
				},
				new Object[]{
						Action.DELETE, false
				},
				new Object[]{
						Action.ADMIN, false	
				}
		};
	}		

	@Test
	@Parameters
	public void testIsAllowedWithUserRankRules(Action action, boolean isAllowed) {		
		AclRole role = getRole("role");
		AclUser allowed = getUser("allowed", 10, role);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(allowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Action.CREATE, new LinkedList<>())
		);
		
		AuthorizationHelper helper = getHelper(mock);
		
		assertEquals(isAllowed, helper.isAllowed(allowed, destination, action));		
	}
	
	public Object[] parametersForTestIsAllowedWithUserRankRules() {
		return new Object[] {
				new Object[]{
						Action.READ, true	
				},
				new Object[]{
						Action.UPDATE, true
				},
				new Object[]{
						Action.CREATE, true
				},
				new Object[]{
						Action.DELETE, false
				},
				new Object[]{
						Action.ADMIN, false	
				}
		};
	}
	
	@Test
	@Parameters
	public void testIsAllowedWithRoleIdRules(Action action, boolean isAllowed) {
		AclRole disallowedRole = getRole("disallowed-role");
		AclRole allowedRole = getRole("allowed-role");
		AclRole allowedRole2 = getRole("allowed-role2");
		AclRole forbiddenRole = getRole("forbidden-role");
		
		AclUser disallowed = getUser("disalowed", 10, disallowedRole);
		AclUser allowed = getUser("allowed", 10, disallowedRole, allowedRole, disallowedRole, allowedRole2);
		AclUser forbidden = getUser("forbidden", 10, forbiddenRole, disallowedRole);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(allowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Action.UNDEFINED, Arrays.asList(
					Action.UNDEFINED, Action.CREATE, Action.UNDEFINED, Action.UPDATE
				)
			)
		);
		when(mock.getRulesForUser(disallowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Action.UNDEFINED, Arrays.asList(Action.UNDEFINED))
		);
		when(mock.getRulesForUser(forbidden, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Action.UNDEFINED, Arrays.asList(
					Action.UNDEFINED, Action.FORBIDDEN
				)
			)
		);
		
		AuthorizationHelper helper = getHelper(mock);
		
		assertFalse(helper.isAllowed(disallowed, destination, action));
		assertFalse(helper.isAllowed(forbidden, destination, action));
		assertEquals(isAllowed, helper.isAllowed(allowed, destination, action));
	}
	
	public Object[] parametersForTestIsAllowedWithRoleIdRules() {
		return new Object[] {
				new Object[]{
						Action.READ, true	
				},
				new Object[]{
						Action.UPDATE, true
				},
				new Object[]{
						Action.CREATE, true
				},
				new Object[]{
						Action.DELETE, false
				},
				new Object[]{
						Action.ADMIN, false	
				}
		};
	}
	
	@Test
	@Ignore
	@Parameters
	public void testIsAllowedSelectBeetweenUserIdRankAndRoleId(
			boolean isAllowed,
			Action tested,
			Action userId,
			Action userRank,
			Action roleId) {
		AclRole role = getRole("role");
		AclUser user = getUser("allowed", 10, role);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(user, destination)).thenReturn(
			new Rules(userId, userRank, Arrays.asList(roleId))
		);
		
		AuthorizationHelper helper = getHelper(mock);
		assertEquals(isAllowed, helper.isAllowed(user, destination, tested));
		
	}	
	
	public Object[] parametersForTestIsAllowedSelectBeetweenUserIdRankAndRoleId() {
		return new Object[] {
				new Object[]{
						Action.READ, true	
				},
				new Object[]{
						Action.UPDATE, true
				},
				new Object[]{
						Action.CREATE, true
				},
				new Object[]{
						Action.DELETE, false
				},
				new Object[]{
						Action.ADMIN, false	
				}
		};
	}
	
	@Test(expected=AccessDeniedException.class)
	public void testThrowIfIsNotAllowedThrows() throws AccessDeniedException {
		AclRole disallowedRole = getRole("disallowed-role");
		AclUser disallowed = getUser("disallowed", 10, disallowedRole);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(disallowed, destination)).thenReturn(
			new Rules(Action.READ, Action.UNDEFINED, new LinkedList<>())
		);
				
		AuthorizationHelper helper = getHelper(mock);
		helper.throwIfIsNotAllowed(
				disallowed,
				destination,
				Action.CREATE
		);
	}
	
	@Test
	public void testThrowIfIsNotAllowedNotThrowsWhenUserIsAllowed() {
		AclRole role = getRole("disallowed-role");
		AclUser allowed = getUser("allowed", 10, role);
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUser(allowed, destination)).thenReturn(
			new Rules(Action.CREATE, Action.UNDEFINED, new LinkedList<>())
		);
				
		AuthorizationHelper helper = getHelper(mock);
		helper.throwIfIsNotAllowed(
				allowed,
				destination,
				Action.READ
		);
		assertTrue(true);
	}
	
	/*** SEPARATOR ***/
	
	
	private AuthorizationHelper getHelper(RulesDao rules) {
		return new AuthorizationHelper(rules, mock(Logger.class));
	}
	
	private AclDestination getDestination(String id) {
		return ()->{return id;};
	}
	
	private AclRole getRole(String id) {
		return ()->{return id;};
	}
	
	private AclUser getUser(String id, int rank, AclRole... aclRoles) {
		return new AclUser() {
			
			@Override
			public List<AclRole> getRoles() {
				return Arrays.asList(aclRoles);
			}
			
			@Override
			public int getRank() {
				return rank;
			}
			
			@Override
			public String getId() {
				return id;
			}
		};
	}
	
}
