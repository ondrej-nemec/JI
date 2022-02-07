package ji.json;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class JsonWritterTest {

	@Test
	public void testWriteJson() {
		Map<String, Object> json = new HashMap<>();
		json.put("aa", Arrays.asList("some-text"));
		json.put("bb", "another-text");
		JsonWritter w = new JsonWritter();
		assertEquals("{\"aa\":[\"some-text\"],\"bb\":\"another-text\"}", w.write(json));
	}
}
