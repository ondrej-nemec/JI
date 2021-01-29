package helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

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
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataIsAllowedReturnFalseIfNoRulesMatch")
	public void testIsAllowedReturnFalseIfNoRulesMatch(Rules rules) {
		AclDestination domain = getDestination("undefined");
		AclRole<String> role = getRole("role");
		AclUser<String, String> user = getUser("user", 10, role);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(rules);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		Action[] actions = new Action[] {Action.ALLOWED, Action.READ, Action.UPDATE, Action.CREATE, Action.DELETE, Action.ADMIN};
		for (Action action : actions) {
			assertEquals(false, helper.isAllowed(user, domain, action));
		}
	}

	public Object[] dataIsAllowedReturnFalseIfNoRulesMatch() {
		return new Object[] {
				new Object[]{null},
				new Object[]{Rules.forUserWithOwner(Action.UNDEFINED, null)},
				new Object[]{Rules.forUserWithOwner(Action.FORBIDDEN, null)},
				new Object[]{Rules.forUserGroupsAndLevels(null)},
				new Object[]{Rules.forUserGroupsAndLevels(Arrays.asList(
					AccessRule.withLevel(0, Action.UNDEFINED, null)
				))},
				new Object[]{Rules.forUserGroupsAndLevels(Arrays.asList(
					AccessRule.withLevel(0, Action.FORBIDDEN, null)
				))},
				new Object[]{Rules.forUserGroupsAndLevels(Arrays.asList(
					AccessRule.withoutLevel(Action.UNDEFINED, null)
				))},
				new Object[]{Rules.forUserGroupsAndLevels(Arrays.asList(
					AccessRule.withoutLevel(Action.FORBIDDEN, null)
				))},
		};
	}
	
	@Test(expected=NotAllowedActionException.class)
	@Parameters(method = "dataIsAllowedThrowsIfNotAllowedActionIsGiven")
	public void testIsAllowedThrowsIfNotAllowedActionIsGiven(Action act) {
		AuthorizationHelper<String, String> helper = getHelper(null);
		helper.isAllowed(null, null, act);
	}
	
	public Object[] dataIsAllowedThrowsIfNotAllowedActionIsGiven() {
		return new Object[] {
				new Object[]{Action.UNDEFINED},
				new Object[]{Action.FORBIDDEN},
		};
	}
	
	/*** SEPARATOR ***/
	
	public Object[] dataIsAllowedTest() {
		return new Object[] {
			new Object[]{Action.ALLOWED, null, true, true},
			new Object[]{Action.ALLOWED, Arrays.asList("owner"), false, true},
			new Object[]{Action.ALLOWED, Arrays.asList("not-owner"), false, false},
				
			new Object[]{Action.READ, null, true, true},
			new Object[]{Action.READ, Arrays.asList("owner"), false, true},
			new Object[]{Action.READ, Arrays.asList("not-owner"), false, false},

			new Object[]{Action.UPDATE, null, true, true},
			new Object[]{Action.UPDATE, Arrays.asList("owner"), false, true},
			new Object[]{Action.UPDATE, Arrays.asList("not-owner"), false, false},

			new Object[]{Action.CREATE, null, true, true},
			new Object[]{Action.CREATE, Arrays.asList("owner"), false, true},
			new Object[]{Action.CREATE, Arrays.asList("not-owner"), false, false},

			new Object[]{Action.DELETE, null, false, false},
			new Object[]{Action.DELETE, Arrays.asList("owner"), false, false},
			new Object[]{Action.DELETE, Arrays.asList("not-owner"), false, false},

			new Object[]{Action.ADMIN, null, false, false},
			new Object[]{Action.ADMIN, Arrays.asList("owner"), false, false},
			new Object[]{Action.ADMIN, Arrays.asList("not-owner"), false, false},
		};
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataIsAllowedTest")
	public void testIsAllowedForUserId(Action action, List<String> owners, boolean allowedWithoutOwner, boolean allowedWithOwner) {
		AclDestination domain = getDestination("for-user-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.forUserWithOwner(Action.CREATE, owners)
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		assertEquals(allowedWithoutOwner, helper.isAllowed(user, domain, action));
		assertEquals(allowedWithOwner, helper.isAllowed(user, domain, action, "owner"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataIsAllowedTest")
	public void testIsAllowedForGroupId(Action action, List<String> owners, boolean allowedWithoutOwner, boolean allowedWithOwner) {
		AclDestination domain = getDestination("for-group-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.forUserGroupsAndLevels(Arrays.asList(
				AccessRule.withoutLevel(Action.CREATE, owners),
				AccessRule.withoutLevel(Action.READ, owners),
				AccessRule.withoutLevel(Action.FORBIDDEN, owners)
			))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		assertEquals(allowedWithoutOwner, helper.isAllowed(user, domain, action));
		assertEquals(allowedWithOwner, helper.isAllowed(user, domain, action, "owner"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataIsAllowedTest")
	public void testIsAllowedForGroupIdAndLevel(Action action, List<String> owners, boolean allowedWithoutOwner, boolean allowedWithOwner) {
		AclDestination domain = getDestination("for-group-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.forUserGroupsAndLevels(Arrays.asList(
				AccessRule.withLevel(12, Action.ADMIN, owners),
				AccessRule.withLevel(10, Action.CREATE, owners),
				AccessRule.withLevel(10, Action.READ, owners),
				AccessRule.withLevel(10, Action.FORBIDDEN, owners)
			))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		assertEquals(allowedWithoutOwner, helper.isAllowed(user, domain, action));
		assertEquals(allowedWithOwner, helper.isAllowed(user, domain, action, "owner"));
	}
	
	/*** SEPARATOR ***/
		/*
		 * rules ==null
		 * rules...getOwner == null
		 * final action == undefined | forbidden
		 */
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataGetAllowedThrowsIfNoRulesMatch")
	public void testGetAllowedThrowsIfNoRulesMatch(Rules rules) {
		AclDestination domain = getDestination("undefined");
		AclRole<String> role = getRole("role");
		AclUser<String, String> user = getUser("user", 10, role);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(rules);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		for (Action action : Action.values()) {
			try {
				helper.allowed(user, domain, action);
				fail("Exception required " + action);
			} catch (NotAllowedActionException e) {
				assertTrue(true);
			}
		}
		
	}

	public Object[] dataGetAllowedThrowsIfNoRulesMatch() {
		return new Object[] {
				new Object[]{null},
				new Object[]{Rules.forUserGroupsAndLevels(Arrays.asList(
					AccessRule.withoutLevel(Action.CREATE, null)
				))},
				new Object[]{Rules.forUserWithOwner(Action.CREATE, null)},
		};
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataGetAllowedForUserId")
	public void testGetAllowedForUserId(Action action, List<String> owners) {
		AclDestination domain = getDestination("for-user-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.forUserWithOwner(Action.CREATE, Arrays.asList("owner1", "owner2", "owner3"))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		if (owners == null) {
			assertEquals(owners, helper.getAllowed(user, domain, action));
		} else {
			Set<String> expected = new HashSet<>();
			expected.addAll(owners);
			assertEquals(expected, helper.getAllowed(user, domain, action));
		}
		
	}
	
	public Object[] dataGetAllowedForUserId() {
		return new Object[] {
			new Object[]{Action.ALLOWED, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.READ, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.UPDATE, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.CREATE, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.DELETE, null},
			new Object[]{Action.ADMIN, null},
		};
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataGetAllowedForGroup")
	public void testGetAllowedForGroup(Action action, List<String> owners) {
		AclDestination domain = getDestination("for-user-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.forUserGroupsAndLevels(Arrays.asList(
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner6")),
				AccessRule.withoutLevel(Action.FORBIDDEN, Arrays.asList("owner5", "owner6")),
				AccessRule.withoutLevel(Action.READ, Arrays.asList("owner5")),
				AccessRule.withoutLevel(Action.CREATE, Arrays.asList("owner1", "owner2")),
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner3")),
				AccessRule.withLevel(12, Action.CREATE, Arrays.asList("owner4"))
			))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		if (owners == null) {
			assertEquals(owners, helper.getAllowed(user, domain, action));
		} else {
			Set<String> expected = new HashSet<>();
			expected.addAll(owners);
			assertEquals(expected, helper.getAllowed(user, domain, action));
		}
		
	}
	
	public Object[] dataGetAllowedForGroup() {
		return new Object[] {
			new Object[]{Action.ALLOWED, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.READ, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.UPDATE, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.CREATE, Arrays.asList("owner1", "owner2", "owner3")},
			new Object[]{Action.DELETE, null},
			new Object[]{Action.ADMIN, null},
		};
	}
	

	@SuppressWarnings("unchecked")
	@Test
	@Parameters(method = "dataGetAllowedFull")
	public void testGetAllowedFull(Action action, List<String> owners) {
		AclDestination domain = getDestination("for-user-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.full(Action.CREATE, Arrays.asList("owner5"), Arrays.asList(
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner6")),
				AccessRule.withoutLevel(Action.FORBIDDEN, Arrays.asList("owner5", "owner6")),
				AccessRule.withoutLevel(Action.READ, Arrays.asList("owner5")),
				AccessRule.withoutLevel(Action.CREATE, Arrays.asList("owner1", "owner2")),
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner3")),
				AccessRule.withLevel(12, Action.CREATE, Arrays.asList("owner4"))
			))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		if (owners == null) {
			assertEquals(owners, helper.getAllowed(user, domain, action));
		} else {
			Set<String> expected = new HashSet<>();
			expected.addAll(owners);
			assertEquals(expected, helper.getAllowed(user, domain, action));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllowedFullWithForbidden() {
		AclDestination domain = getDestination("for-user-id");
		AclUser<String, String> user = getUser("user", 10);
		
		RulesDao<String, String> mock = mock(RulesDao.class);
		when(mock.getRulesForUserAndGroups(user, domain)).thenReturn(
			Rules.full(Action.FORBIDDEN, Arrays.asList("owner3"), Arrays.asList(
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner6")),
				AccessRule.withoutLevel(Action.FORBIDDEN, Arrays.asList("owner5", "owner6")),
				AccessRule.withoutLevel(Action.READ, Arrays.asList("owner5")),
				AccessRule.withoutLevel(Action.CREATE, Arrays.asList("owner1", "owner2")),
				AccessRule.withLevel(9, Action.CREATE, Arrays.asList("owner3")),
				AccessRule.withLevel(12, Action.CREATE, Arrays.asList("owner4"))
			))
		);
		
		AuthorizationHelper<String, String> helper = getHelper(mock);
		
		Set<String> expected = new HashSet<>();
		expected.addAll(Arrays.asList("owner1", "owner2"));
		assertEquals(expected, helper.getAllowed(user, domain, Action.CREATE));
	}
	
	public Object[] dataGetAllowedFull() {
		return new Object[] {
			new Object[]{Action.ALLOWED, Arrays.asList("owner1", "owner2", "owner3", "owner5")},
			new Object[]{Action.READ, Arrays.asList("owner1", "owner2", "owner3", "owner5")},
			new Object[]{Action.UPDATE, Arrays.asList("owner1", "owner2", "owner3", "owner5")},
			new Object[]{Action.CREATE, Arrays.asList("owner1", "owner2", "owner3", "owner5")},
			new Object[]{Action.DELETE, null},
			new Object[]{Action.ADMIN, null},
		};
	}
	
	/*** SEPARATOR ***/
	
	
	private AuthorizationHelper<String, String> getHelper(RulesDao<String, String> rules) {
		return new AuthorizationHelper<>(rules, mock(Logger.class));
	}
	
	private AclDestination getDestination(String id) {
		return new AclDestination() {
			
			@Override
			public String getId() {
				return id;
			}
			
			@Override
			public String toString() {
				return String.format("Role #%s", id);
			}
		};
	}
	
	private AclRole<String> getRole(String id) {
		return new AclRole<String>() {
			
			@Override
			public String getId() {
				return id;
			}
			
			@Override
			public String toString() {
				return String.format("Role #%s", id);
			}
		};
	}
	
	private AclUser<String, String> getUser(String id, int rank, AclRole<String>... aclRoles) {
		return new AclUser<String, String>() {
			
			@Override
			public List<AclRole<String>> getRoles() {
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
			public String toString() {
				return String.format("User #%s(%s): %s", getId(), getRank(), getRoles());
			}
		};
	}
}
