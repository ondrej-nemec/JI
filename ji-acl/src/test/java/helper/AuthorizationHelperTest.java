package helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;

import exception.AccessDeniedException;
import helper.implementations.TestDestination;
import helper.implementations.TestRole;
import helper.implementations.TestUser;
import interfaces.AclDestination;
import interfaces.AclRules;
import interfaces.AclUser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import common.ILogger;

@RunWith(JUnitParamsRunner.class)
public class AuthorizationHelperTest {

	@Test
	@Parameters
	public void testIsAllowedWorks(AclUser user, AclDestination dest, Action act, boolean isAllowed) {
		AuthorizationHelper helper = getHelper();
		assertEquals(
				isAllowed,
				helper.isAllowed(user, dest, act)
		);
	}
	
	public Object[] parametersForTestIsAllowedWorks() {
		return new Object[] {
				new Object[]{
						new TestUser("undefined", 0, Arrays.asList(
								new TestRole("undefinedR", 0)
								)),
						new TestDestination("main page"),
						Action.READ,
						false
					},
					new Object[]{
						new TestUser("forbidden", 0, Arrays.asList(
								)),
						new TestDestination("main page"),
						Action.READ,
						false
					},
					/**********/
					new Object[]{
						new TestUser("userId", 0, Arrays.asList(
								)),
						new TestDestination("list"),
						Action.READ,
						true
					},
					new Object[]{
						new TestUser("user rank", 30, Arrays.asList(
								)),
						new TestDestination("list"),
						Action.READ,
						true
					},
					new Object[]{
						new TestUser("role id", 0, Arrays.asList(
								new TestRole("roleId", 0)
								)),
						new TestDestination("list"),
						Action.READ,
						true
					},
					new Object[]{
						new TestUser("role rank", 0, Arrays.asList(
								new TestRole("roleRank", 30)
								)),
						new TestDestination("list"),
						Action.READ,
						true
					},
					/*************/
					new Object[]{
						new TestUser("userId", 0, Arrays.asList(
								new TestRole("undefinedR", 0)
								)),
						new TestDestination("list"),
						Action.READ,
						true
					},
					new Object[]{
						new TestUser("oneRoleAllowedSecondNot", 0, Arrays.asList(
								new TestRole("allowed", 0),
								new TestRole("disallowed", 0)
								)),
						new TestDestination("article"),
						Action.READ,
						true
					},
					new Object[]{
						new TestUser("disallowed", 0, Arrays.asList(
								new TestRole("allowed", 0)
								)),
						new TestDestination("article"),
						Action.READ,
						false
					}
			};
	}
	
	@Test(expected=AccessDeniedException.class)
	public void testThrowIfIsNotAllowedThrows() throws AccessDeniedException {
		AuthorizationHelper helper = getHelper();
		helper.throwIfIsNotAllowed(
				new TestUser("forbidden", 0, Arrays.asList(
				)),
				new TestDestination("main page"),
				Action.READ
		);
	}
	
	@Test
	public void testThrowIfIsNotAllowedNotThrowsWhenUserIsAllowed() throws AccessDeniedException {
		AuthorizationHelper helper = getHelper();
		helper.throwIfIsNotAllowed(
				new TestUser("userId", 0, Arrays.asList(
						)),
				new TestDestination("list"),
				Action.READ
		);
		assertTrue(true);
	}
	
	private AuthorizationHelper getHelper() {
		return new AuthorizationHelper(getRules(), mock(ILogger.class));
	}

	private AclRules getRules() {
		AclRules mock = mock(AclRules.class);
		
		when(mock.getRuleUserId("undefined", "main page", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleUserRank(0, "main page", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleId("undefinedR", "main page", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleRank(0, "main page", Action.READ)).thenReturn(Status.UNSPECIFIED);
		
		when(mock.getRuleUserId("forbidden", "main page", Action.READ)).thenReturn(Status.DISALLOWED);
		/******/
		when(mock.getRuleUserId("userId", "list", Action.READ)).thenReturn(Status.ALLOWED);
		
		when(mock.getRuleUserId("user rank", "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleUserRank(30, "list", Action.READ)).thenReturn(Status.ALLOWED);
		
		when(mock.getRuleUserId("role id", "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleUserRank(0, "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleId("roleId", "list", Action.READ)).thenReturn(Status.ALLOWED);
		
		when(mock.getRuleUserId("role rank", "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleUserRank(0, "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleId("roleRank", "list", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleRank(30, "list", Action.READ)).thenReturn(Status.ALLOWED);
		/************/
		when(mock.getRuleUserId("oneRoleAllowedSecondNot", "article", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleUserRank(0, "article", Action.READ)).thenReturn(Status.UNSPECIFIED);
		when(mock.getRuleRoleId("allowed", "article", Action.READ)).thenReturn(Status.ALLOWED);
		when(mock.getRuleRoleId("disallowed", "article", Action.READ)).thenReturn(Status.DISALLOWED);
		
		when(mock.getRuleUserId("disallowed", "article", Action.READ)).thenReturn(Status.DISALLOWED);
		return mock;
	}

	
	public static Collection<Object[]> getDataSet() {
		return Arrays.asList(
				
			);
		}
	
}
