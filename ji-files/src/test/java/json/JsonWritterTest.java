package json;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import common.structures.MapInit;
import json.annotations.JsonIgnored;
import json.annotations.JsonParameter;

public class JsonWritterTest {

	@Test
	public void testWriteJson() {
		Map<String, Object> json = new HashMap<>();
		json.put("aa", Arrays.asList("some-text"));
		json.put("bb", "another-text");
		JsonWritter w = new JsonWritter();
		assertEquals("{\"aa\":[\"some-text\"],\"bb\":\"another-text\"}", w.write(json));
	}

	@Test
	public void testToJsonAllAttributes() {
		Object actual = new Object() {
			@SuppressWarnings("unused")
			private String first = "first value";
			@SuppressWarnings("unused")
			private int second = 42;
			@SuppressWarnings("unused")
			private List<String> list = Arrays.asList("a", "b", "c");
		};
		Map<String, Object> expected = new MapInit<String, Object>()
				.append("first", "first value")
				.append("second", 42)
				.append("list", Arrays.asList("a", "b", "c"))
				.toMap();
		assertEquals(expected, new JsonWritter().refrectionObject(actual));
	}

	@Test
	public void testToJsonAnnotatedAttributes() {
		Object actual = new Object() {
			@SuppressWarnings("unused")
			private String first = "first value";
			@SuppressWarnings("unused")
			private int second = 42;
			@SuppressWarnings("unused")
			private List<String> list = Arrays.asList("a", "b", "c");
			@JsonIgnored
			private double ignored = 12.3;
			@JsonParameter("realName")
			private String anotherName = "renamed";
		};
		Map<String, Object> expected = new MapInit<String, Object>()
				.append("first", "first value")
				.append("second", 42)
				.append("list", Arrays.asList("a", "b", "c"))
				.append("realName", "renamed")
				.toMap();
		assertEquals(expected, new JsonWritter().refrectionObject(actual));
	}
}
