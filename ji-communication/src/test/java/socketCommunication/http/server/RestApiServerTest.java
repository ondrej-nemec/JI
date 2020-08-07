package socketCommunication.http.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.util.Properties;


import org.junit.Test;
import org.junit.runner.RunWith;

import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.session.Session;
import socketCommunication.http.server.session.SessionStorage;
import socketCommunication.http.server.RestApiServer;

@RunWith(JUnitParamsRunner.class)
public class RestApiServerTest {

	@Test
	@Parameters(method = "dataParseFirstSplitFirstLine")
	public void testParseFirstSplitFirstLine(String line, Properties expectedRequest, Properties expectedParams) {
		RestApiServer api = getApi();
		
		Properties actualRequest = new Properties();
		Properties actualParams = new Properties();
		api.parseFirst(actualRequest, actualParams, line);
		
		assertEquals(expectedRequest, actualRequest);
		assertEquals(expectedParams, actualParams);
	}
	
	public Object[] dataParseFirstSplitFirstLine() {
		return new Object[] {
				new Object[] {
						"",
						properties(),
						properties()
				},
				new Object[] {
						"method url protocol",
						properties(
								t(RestApiServer.METHOD, "method"),
								t(RestApiServer.URL, "url"),
								t(RestApiServer.FULL_URL, "url"),
								t(RestApiServer.PROTOCOL, "protocol")
						),
						properties()
				},
				new Object[] {
						"method url?aa=bb protocol",
						properties(
								t(RestApiServer.METHOD, "method"),
								t(RestApiServer.URL, "url"),
								t(RestApiServer.FULL_URL, "url?aa=bb"),
								t(RestApiServer.PROTOCOL, "protocol")
						),
						properties(t("aa", "bb"))
				},
		};
	}
	
	@Test
	@Parameters(method = "dataParseHeaderLineFillHeaderProperties")
	public void testParseHeaderLineFillHeaderProperties(Properties expected, String line) {
		RestApiServer api = getApi();
		Properties actual = new Properties();
		api.parseHeaderLine(line, actual);
		assertEquals(expected, actual);
	}
	
	public Object[] dataParseHeaderLineFillHeaderProperties() {
		return new Object[] {
			new Object[] {
				properties(t("aa", "bb")),
				"aa: bb"
			},
			new Object[] {
				properties(),
				""
			},
			new Object[] {
				properties(),
				": "
			},
			new Object[] {
				properties(t("aa", "bb: ")),
				"aa: bb: "
			},
			new Object[] {
				properties(t("aa", "")),
				"aa: "
			},
			new Object[] {
				properties(t("aa", "")),
				"aa"
			},
		};
	}
	
	@Test
	@Parameters(method = "dataParsePayloadFillParamsProperties")
	public void testParsePayloadFillParamsProperties(Properties expected, String payload) {
		RestApiServer api = getApi();
		Properties actual = new Properties();
		api.parsePayload(actual, payload);
		assertEquals(expected, actual);
	}
	
	public Object[] dataParsePayloadFillParamsProperties() {
		return new Object[] {
			new Object[] {
				properties(t("aa", "bb")),
				"aa=bb"
			},
			new Object[] {
				properties(),
				""
			},
			new Object[] {
				properties(),
				"&"
			},
			new Object[] {
				properties(),
				"=&="
			},
			new Object[] {
				properties(t("aa", "bb")),
				"aa=bb&"
			},
			new Object[] {
				properties(t("aa", "")),
				"aa="
			},
			new Object[] {
					properties(t("aa", "")),
					"aa"
				},
			new Object[] {
				properties(t("aa", "bb"), t("cc", "dd")),
				"aa=bb&cc=dd"
			},
			new Object[] {
				properties(t("dd", "ee")),
				"aa=bb=cc&dd=ee"
			}
		};
	}
	
	@Test
	@Parameters(method="dataGetSessionidFromCookieHeaderReturnsCorrectId")
	public void testGetSessionidFromCookieHeaderReturnsCorrectId(Object cookiesString, String expected) {
		RestApiServer api = getApi();
		assertEquals(expected, api.getSessionIdFromCookieHeader(cookiesString));
	}
	
	public Object[] dataGetSessionidFromCookieHeaderReturnsCorrectId() {
		return new Object[] {
			new Object[] {
				null,
				null
			},
			new Object[] {
					"not-valid-text",
					null
				},
			new Object[] {
					"not;valid",
					null
				},
			new Object[] {
					"missing=correct;key=cookie",
					null
				},
			new Object[] {
					"SessionID=123456",
					"123456"
				},
			new Object[] {
					"SessionID=123456;another=value",
					"123456"
				},
			new Object[] {
					"SessionID = 123456 ; another=value",
					"123456"
				}
		};
	}
	
	@Test
	@Parameters(method = "dataGetSessionReturnsCorrectSession")
	public void testGetSessionReturnsCorrectSession(Session inStorage, Session expected, int removeCount, int addCount) {
		SessionStorage storage = mock(SessionStorage.class);
		when(storage.getSession(any())).thenReturn(inStorage);
		
		RestApiServer api = new RestApiServer(
				50,
				mock(RestApiServerResponseFactory.class),
				storage,
				mock(Logger.class)
		);
		Properties header = new Properties();
		header.put("Cookie", "SessionID=-session-id-");
		Session actual = api.getSession(header, "ip", 100);
		assertEquals(expected.getExpirationTime(), actual.getExpirationTime());
		assertEquals(expected.getIp(), actual.getIp());
		
		verify(storage, times(1)).getSession("-session-id-");
		verify(storage, times(addCount)).addSession(any());//expected
		verify(storage, times(removeCount)).removeSession("-session-id-");
		verifyNoMoreInteractions(storage);
	}
	
	public Object[] dataGetSessionReturnsCorrectSession() {
		return new Object[] {
			new Object[] {
					null,
					new Session("", "ip", 150, ""),
					0, 1
			},
			new Object[] {
					new Session("-session-id-", "ip2", 125, ""),
					new Session("", "ip2", 150, ""),
					0, 1
			},
			new Object[] {
					new Session("-session-id-", "ip", 75, ""),
					new Session("", "", 0, ""),
					1, 0
			}
		};
	}
	
	/***********************/
	
	private RestApiServer getApi() {
		return new RestApiServer(
				0,
				mock(RestApiServerResponseFactory.class),
				mock(SessionStorage.class),
				mock(Logger.class)
		);
	}

}
