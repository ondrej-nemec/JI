package socketCommunication.http.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.util.Optional;
import java.util.Properties;


import org.junit.Test;
import org.junit.runner.RunWith;

import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.RestApiServer;

@RunWith(JUnitParamsRunner.class)
public class RestApiServerTest {

	// TODO test file upload
	
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
	/***********************/
	
	private RestApiServer getApi() {
		return new RestApiServer(
				mock(RestApiServerResponseFactory.class),
				0,
				Optional.empty(),
				mock(Logger.class)
		);
	}

}
