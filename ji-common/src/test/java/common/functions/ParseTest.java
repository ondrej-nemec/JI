package common.functions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import common.annotations.ParseIgnored;
import common.annotations.ParseParameter;
import common.functions.testingClasses.Main;
import common.structures.MapInit;

public class ParseTest {

	@Test
	public void testToJsonAllAttributes() { // TODO rename
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
		assertEquals(expected, Parse.stringify(actual));
	}

	@Test
	public void testToJsonAnnotatedAttributes() { // TODO rename
		Object actual = new Object() {
			@SuppressWarnings("unused")
			private String first = "first value";
			@SuppressWarnings("unused")
			private int second = 42;
			@SuppressWarnings("unused")
			private List<String> list = Arrays.asList("a", "b", "c");
			@ParseIgnored
			private double ignored = 12.3;
			@ParseParameter("realName")
			private String anotherName = "renamed";
		};
		Map<String, Object> expected = new MapInit<String, Object>()
				.append("first", "first value")
				.append("second", 42)
				.append("list", Arrays.asList("a", "b", "c"))
				.append("realName", "renamed")
				.toMap();
		assertEquals(expected, Parse.stringify(actual));
	}
	
	@Test
	public void testReadObject() throws Exception { // TODO rename
		assertEquals(new Main(true), Parse.read(
			Main.class, 
			new MapInit<String, Object>()
				.append("first", 42)
				.append("second", "Hello World!")
				.append("list", Arrays.asList(
					"aaa",
					95/*,
					new MapInit<String, Object>()
						.append("text1", "text1")
						.append("text2", "text2")
						.toMap()*/
				))
				.append("map", new MapInit<String, Object>()
					.append("item1", 123)
					.append("item2", true)
					.toMap()
				)
				.append("sub1", new MapInit<String, Object>()
						.append("text1", "text1")
						.append("text2", "text2")
						.toMap()
				)
				.append("subs", Arrays.asList(new MapInit<String, Object>()
						.append("text1", "text1")
						.append("text2", "text2")
						.toMap()))
				.toMap())
		);
	}
}
