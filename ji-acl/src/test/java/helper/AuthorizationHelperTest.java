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
import interfaces.AclRules;
import interfaces.AclUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import common.Logger;

@RunWith(JUnitParamsRunner.class)
public class AuthorizationHelperTest {
	
	@Test
	@Parameters
	public void testIsAllowedReturnFalseIfNoRulesGiven(Action action) {
		String domain = "undefined";
		AclRole role = getRole("role");
		AclUser user = getUser("user", 10, role);
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(user.getId(), domain)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForUserRank(user.getRank(), domain)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForRoleId(role.getId(), domain)).thenReturn(Action.UNDEFINED);
		
		AuthorizationHelper helper = getHelper(mock);
		assertEquals(
				false,
				helper.isAllowed(user, getDestination(domain), action)
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(
				allowed.getId(),
				destination.getId()
			)).thenReturn(Action.CREATE);
		
		when(mock.getActionForUserId(
				forbidden.getId(), 
				destination.getId()
			)).thenReturn(Action.FORBIDDEN);
		
		when(mock.getActionForUserId(
				disallowed.getId(),
				destination.getId()
			)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForUserRank(
				disallowed.getRank(),
				destination.getId()
			)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForRoleId(
				role.getId(),
				destination.getId()
			)).thenReturn(Action.UNDEFINED);
		
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(
				allowed.getId(),
				destination.getId()
			)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForUserRank(
				allowed.getRank(),
				destination.getId()
			)).thenReturn(Action.CREATE);
		
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(anyString(), anyString())).thenReturn(Action.UNDEFINED);
		when(mock.getActionForUserRank(anyInt(), anyString())).thenReturn(Action.UNDEFINED);		
		
		when(mock.getActionForRoleId(
				allowedRole.getId(),
				destination.getId()
			)).thenReturn(Action.CREATE);
		when(mock.getActionForRoleId(
				allowedRole2.getId(),
				destination.getId()
			)).thenReturn(Action.UPDATE);
		when(mock.getActionForRoleId(
				disallowedRole.getId(),
				destination.getId()
			)).thenReturn(Action.UNDEFINED);
		when(mock.getActionForRoleId(
				forbiddenRole.getId(), 
				destination.getId()
			)).thenReturn(Action.FORBIDDEN);
		
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForRoleId(
				role.getId(),
				destination.getId()
			)).thenReturn(roleId);
		when(mock.getActionForUserRank(
				user.getRank(),
				destination.getId()
			)).thenReturn(userRank);
		when(mock.getActionForUserId(
				user.getId(), 
				destination.getId()
			)).thenReturn(userId);
		
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(
				disallowed.getId(),
				destination.getId()
			)).thenReturn(Action.READ);
				
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
		
		AclRules mock = mock(AclRules.class);
		when(mock.getActionForUserId(
				allowed.getId(),
				destination.getId()
			)).thenReturn(Action.CREATE);
				
		AuthorizationHelper helper = getHelper(mock);
		helper.throwIfIsNotAllowed(
				allowed,
				destination,
				Action.READ
		);
		assertTrue(true);
	}
	
	/*** SEPARATOR ***/
	
	
	private AuthorizationHelper getHelper(AclRules rules) {
		return new AuthorizationHelper(rules, mock(Logger.class));
	}
	
	private AclDestination getDestination(String id) {
		return new AclDestination() {
			
			@Override
			public String getId() {
				return id;
			}
			
			@Override
			public boolean equals(AclDestination destination) {
				return getId() == destination.getId();
			}
		};
	}
	
	private AclRole getRole(String id) {
		return new AclRole() {
			
			@Override
			public String getId() {
				return id;
			}
			
			@Override
			public boolean equals(AclRole role) {
				return getId() == role.getId();
			}
		};
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
			
			@Override
			public boolean equals(AclUser user) {
				return getId() == user.getId();
			}
		};
	}
	
}
