package helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
		
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			new Rules(Action.UNDEFINED, Arrays.asList())
		);
		
		AuthorizationHelper helper = getHelper(mock);
		assertEquals(false, helper.isAllowed(user, domain, action));
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
		when(mock.getRulesForUserAndGroups(allowed, destination)).thenReturn(
			new Rules(Action.CREATE)
		);
		
		when(mock.getRulesForUserAndGroups(forbidden, destination)).thenReturn(
			new Rules(Action.FORBIDDEN)
		);
		
		when(mock.getRulesForUserAndGroups(disallowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED)
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
	@Parameters(method = "dataIsAllowedWithAccessRules")
	public void testIsAllowedWithAccessRules(Action action, boolean isAllowed, int rank) {
		AclUser allowed = getUser("allowed", rank, getRole(""));
		AclDestination destination = getDestination("user-id-rule");
		
		RulesDao mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(allowed, destination)).thenReturn(
			new Rules(Arrays.asList(
					new AccessRule(null, Action.UNDEFINED),
					new AccessRule(null, Action.CREATE),
					new AccessRule(15, Action.UNDEFINED),
					new AccessRule(null, Action.UPDATE),
					new AccessRule(15, Action.DELETE)
			))
		);
		
		AuthorizationHelper helper = getHelper(mock);
		assertEquals(isAllowed, helper.isAllowed(allowed, destination, action));
	}
	
	public Object[] dataIsAllowedWithAccessRules() {
		return new Object[] {
				new Object[]{
						Action.READ, true, 10	
				},
				new Object[]{
						Action.UPDATE, true, 10
				},
				new Object[]{
						Action.CREATE, true, 10
				},
				new Object[]{
						Action.DELETE, false, 10
				},
				new Object[]{
						Action.DELETE, true, 15
				},
				new Object[]{
						Action.ADMIN, false	, 10
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
		when(mock.getRulesForUserAndGroups(allowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Arrays.asList(
					new AccessRule(null, Action.UNDEFINED),
					new AccessRule(null, Action.CREATE),
					new AccessRule(null, Action.UNDEFINED),
					new AccessRule(null, Action.UPDATE)
			))
		);
		when(mock.getRulesForUserAndGroups(disallowed, destination)).thenReturn(
			new Rules(Action.UNDEFINED, Arrays.asList(new AccessRule(null, Action.UNDEFINED)))
		);
		when(mock.getRulesForUserAndGroups(forbidden, destination)).thenReturn(
			new Rules(Action.UNDEFINED,Arrays.asList(
					new AccessRule(null, Action.UNDEFINED),
					new AccessRule(null, Action.FORBIDDEN)
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
		when(mock.getRulesForUserAndGroups(user, destination)).thenReturn(
			new Rules(userId, Arrays.asList(new AccessRule(10, userRank)))
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
		when(mock.getRulesForUserAndGroups(disallowed, destination)).thenReturn(
			new Rules(Action.READ, Arrays.asList(new AccessRule(null, Action.UNDEFINED)))
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
		when(mock.getRulesForUserAndGroups(allowed, destination)).thenReturn(
			new Rules(Action.CREATE, Arrays.asList(new AccessRule(null, Action.UNDEFINED)))
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
