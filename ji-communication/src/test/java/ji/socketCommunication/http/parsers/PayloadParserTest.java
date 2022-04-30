package ji.socketCommunication.http.parsers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.common.Logger;
import ji.common.structures.MapInit;
import ji.socketCommunication.http.parsers.PayloadParser;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class PayloadParserTest {

	@Test
	@Parameters(method="dataParseParamsWorks")
	public void testCreatePayloadWorks(String expected, Map<String, Object> payload) throws UnsupportedEncodingException {
		PayloadParser parser = new PayloadParser(mock(Logger.class));
		String actual = parser.createPayload(payload);
		assertEquals(expected, actual);
	}

	@Test
	@Parameters(method="dataParseParamsWorks")
	public void testParseParamsWorks(String payload, Map<String, Object> expected) throws UnsupportedEncodingException {
		Map<String, Object> actual = new HashMap<>();
		PayloadParser parser = new PayloadParser(mock(Logger.class));
		parser.parsePayload((n, v)->actual.put(n, v), k->actual.get(k), payload);
		assertEquals(expected, actual);
	}
	
	public Object[] dataParseParamsWorks() {
		Object full = new MapInit<String, Object>()
				.append(
						"list", 
						Arrays.asList(
							"value-list-1", 
							"value-list-2", 
							"value-list-3"
						)
					)
					.append(
						"map",
						new MapInit<>()
						.append("a", "value-map-a")
						.append("b", "value-map-b")
						.toMap()
					)
					.append(
						"maplist",
						new MapInit<>()
						.append(
							"a", 
							Arrays.asList(
								"value-maplist-a-1", 
								"value-maplist-a-2"
							)
						)
						.append(
							"b", 
							Arrays.asList(
								"value-maplist-b-1", 
								"value-maplist-b-2"
							)
						)
						.toMap()
					)
					.toMap();
		return new Object[] {
			new Object[] {
				"key=",
				new MapInit<String, Object>()
				.append("key", "")
				.toMap()
			},
			new Object[] {
				"key=value",
				new MapInit<String, Object>()
				.append("key", "value")
				.toMap()
			},
			new Object[] {
				"key1=value1&key2=value2",
				new MapInit<String, Object>()
				.append("key1", "value1")
				.append("key2", "value2")
				.toMap()
			},
			new Object[] {
				"list[]=value1",
				new MapInit<String, Object>()
				.append("list", Arrays.asList("value1"))
				.toMap()
			},
			new Object[] {
				"list[]=value1&list[]=value2",
				new MapInit<String, Object>()
				.append("list", Arrays.asList("value1", "value2"))
				.toMap()
			},
			new Object[] {
				"map[a]=value1",
				new MapInit<String, Object>()
				.append(
					"map",
					new MapInit<>()
					.append("a", "value1")
					.toMap()
				)
				.toMap()
			},
			new Object[] {
				"map[a]=value1&map[b]=value2",
				new MapInit<String, Object>()
				.append(
					"map",
					new MapInit<>()
					.append("a", "value1")
					.append("b", "value2")
					.toMap()
				)
				.toMap()
			},
			new Object[] {
				"map[a][]=value",
				new MapInit<String, Object>()
				.append("map", new MapInit<>("a", Arrays.asList("value")).toMap())
				.toMap()
			},
			new Object[] {
				"map[a][]=value1&map[a][]=value2",
				new MapInit<String, Object>()
				.append("map", new MapInit<>("a", Arrays.asList("value1", "value2")).toMap())
				.toMap()
			},
			new Object[] {
				  "maplist[a][]=value-maplist-a-1&maplist[a][]=value-maplist-a-2"
				+ "&maplist[b][]=value-maplist-b-1&maplist[b][]=value-maplist-b-2"
				+ "&list[]=value-list-1&list[]=value-list-2&list[]=value-list-3"
				+ "&map[a]=value-map-a&map[b]=value-map-b",
				full
			},
			// TODO check format
			/*new Object[] {
				"maplist=%7Ba%3D%5Bvalue-maplist-a-1%2C+value-maplist-a-2%5D%2C+b%3D%5Bvalue-maplist-b-1%2C+value-maplist-b-2%5D%7D&"
				+ "list=%5Bvalue-list-1%2C+value-list-2%2C+value-list-3%5D&map=%7Ba%3Dvalue-map-a%2C+b%3Dvalue-map-b%7D",
				full
			},*/
		};
	}
	
}
