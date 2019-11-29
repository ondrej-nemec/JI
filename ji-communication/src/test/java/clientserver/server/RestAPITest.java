package clientserver.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.util.Properties;


import org.junit.Test;
import org.junit.runner.RunWith;

import clientserver.server.restapi.CreateRestAPIResponce;
import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class RestAPITest {
	
	@Test
	public void testPaseRequestFillProperties() {
		fail();
	}
	
	@Test
	@Parameters(method = "dataParseFirstSplitFirstLine")
	public void testParseFirstSplitFirstLine(String line, Properties expectedRequest, Properties expectedParams) {
		RestAPI api = getApi();
		
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
								t(RestAPI.METHOD, "method"),
								t(RestAPI.URL, "url"),
								t(RestAPI.FULL_URL, "url"),
								t(RestAPI.PROTOCOL, "protocol")
						),
						properties()
				},
				new Object[] {
						"method url?aa=bb protocol",
						properties(
								t(RestAPI.METHOD, "method"),
								t(RestAPI.URL, "url"),
								t(RestAPI.FULL_URL, "url?aa=bb"),
								t(RestAPI.PROTOCOL, "protocol")
						),
						properties(t("aa", "bb"))
				},
		};
	}
	
	@Test
	@Parameters(method = "dataParseHeaderLineFillHeaderProperties")
	public void testParseHeaderLineFillHeaderProperties(Properties expected, String line) {
		RestAPI api = getApi();
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
		RestAPI api = getApi();
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
	public void testUnEscapeTextReplacePercentages() {
		RestAPI api = getApi();
		assertEquals("a?b/c\\d:e f=g&h%i", api.unEscapeText("a%3Fb%2Fc%5Cd%3Ae+f%3Dg%26h%25i"));
	}
	
	/***********************/
	
	private RestAPI getApi() {
		return new RestAPI(mock(CreateRestAPIResponce.class), mock(Logger.class));
	}

}
