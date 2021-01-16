package socketCommunication.http.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;


import org.junit.Test;
import org.junit.runner.RunWith;

import common.Logger;
import common.structures.Tuple2;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import socketCommunication.http.server.RestApiServerResponseFactory;
import socketCommunication.http.server.RestApiServer;

@RunWith(JUnitParamsRunner.class)
public class RestApiServerTest {

	// TODO test file upload
	
	@Test
	@Parameters(method = "dataParseFirstSplitFirstLine")
	public void testParseFirstSplitFirstLine(String line, Properties expectedRequest, RequestParameters expectedParams) throws UnsupportedEncodingException {
		RestApiServer api = getApi();
		
		Properties actualRequest = new Properties();
		RequestParameters actualParams = new RequestParameters();
		api.parseFirst(actualRequest, actualParams, line);
		
		assertEquals(expectedRequest, actualRequest);
		assertEquals(expectedParams, actualParams);
	}
	
	public Object[] dataParseFirstSplitFirstLine() {
		return new Object[] {
				new Object[] {
						"",
						properties(),
						new RequestParameters()
				},
				new Object[] {
						"method url protocol",
						properties(
								t(RestApiServer.METHOD, "method"),
								t(RestApiServer.URL, "url"),
								t(RestApiServer.FULL_URL, "url"),
								t(RestApiServer.PROTOCOL, "protocol")
						),
						new RequestParameters()
				},
				new Object[] {
						"method url?aa=bb protocol",
						properties(
								t(RestApiServer.METHOD, "method"),
								t(RestApiServer.URL, "url"),
								t(RestApiServer.FULL_URL, "url?aa=bb"),
								t(RestApiServer.PROTOCOL, "protocol")
						),
						new RequestParameters(t("aa", "bb"))
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
	public void testParsePayloadFillParamsProperties(RequestParameters expected, String payload) throws UnsupportedEncodingException {
		RestApiServer api = getApi();
		RequestParameters actual = new RequestParameters();
		api.parsePayload(actual, payload);
		assertEquals(expected, actual);
	}
	
	public Object[] dataParsePayloadFillParamsProperties() {
		return new Object[] {
			new Object[] {
				new RequestParameters(t("aa", "bb")),
				"aa=bb"
			},
			new Object[] {
				new RequestParameters(),
				""
			},
			new Object[] {
				new RequestParameters(),
				"&"
			},
			new Object[] {
				new RequestParameters(),
				"=&="
			},
			new Object[] {
				new RequestParameters(t("aa", "bb")),
				"aa=bb&"
			},
			new Object[] {
				new RequestParameters(t("aa", "")),
				"aa="
			},
			new Object[] {
					new RequestParameters(t("aa", "")),
					"aa"
				},
			new Object[] {
				new RequestParameters(t("aa", "bb"), t("cc", "dd")),
				"aa=bb&cc=dd"
			},
			new Object[] {
				new RequestParameters(t("dd", "ee")),
				"aa=bb=cc&dd=ee"
			},
			/*************
			new Object[] {
					new RequestParameters(new Tuple2<>("number", 1)),
					"number=1"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("boolean", true)),
					"boolean=true"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("string", "text")),
					"string=text"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("double", 12.3)),
					"double=12.3"
				},*/
			new Object[] {
					new RequestParameters(t("aa", "bb"), t("%", "&"), t("dd", "ee")),
					"aa=bb&%25=%26&dd=ee"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("list", Arrays.asList("true", "b", "3"))),
					"list[]=true&list[]=b&list[]=3"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("list", Arrays.asList("true", "b", "3", ""))),
					"list[]=true&list[]=b&list[]=3&list[]="
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("list", Arrays.asList(Arrays.asList("true", "b", "3")))),
					"list[][]=true&list[][]=b&list[][]=3"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("array", hashMap(
						t("first", "true"),
						t("second", "b"),
						t("third", "3")
					))),
					"array[first]=true&array[second]=b&array[third]=3"
				},
			new Object[] {
					new RequestParameters(new Tuple2<>("array", hashMap(
						new Tuple2<>("abc", hashMap(
								t("first", true),
								t("second", "b"),
								t("third", 3),
								t("four", "")
						))
					))),
					"array[abc][first]=true&array[abc][second]=b&array[abc][third]=3&array[abc][four]="
				}
		};
	}
	/*
	@Test
	public void test() throws UnsupportedEncodingException {
		List<String> first = new LinkedList<>();
		first.addAll(Arrays.asList("1", "2", "3"));
		List<String> second = new LinkedList<>();
		second.addAll(Arrays.asList("7", "8", "9"));
		RequestParameters expected = new RequestParameters(
				new Tuple2<>("aa", first),
				new Tuple2<>("b", MapInit.hashMap(
					MapInit.t("a", "4"),
					MapInit.t("b", "5"),
					MapInit.t("c", "6")
				)),
				new Tuple2<>("c", MapInit.hashMap(
					MapInit.t("a", second)
				))
		);
		String payload = "aa[]=1&aa[]=2&aa[]=3&b[a]=4&b[b]=5&b[c]=6&c[a][]=7&c[a][]=8&c[a][]=9";
		RestApiServer api = getApi();
		RequestParameters actual = new RequestParameters();
		api.parsePayload(actual, payload);
		System.out.println(expected);
		System.out.println(actual);
		//System.out.println();
		assertEquals(expected, actual);
	}
	//*/
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
