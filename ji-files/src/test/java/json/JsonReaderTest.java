package json;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import common.MapInit;

public class JsonReaderTest {

	@Test
	public void testReadWorks() throws JsonStreamException {
		Map<String, Object> expected = new HashMap<>();
		expected.put("value", 123);
		expected.put("list", Arrays.asList(1,2,3));
		expected.put("object", MapInit.hashMap(MapInit.t("a", null), MapInit.t("b", "aaa")));
		String json = "{\"value\":123, \"list\":[1,2,3],\"object\":{\"a\":null, \"b\": \"aaa\"}}";
		assertEquals(expected, new JsonReader().read(json));
	}
	
}
