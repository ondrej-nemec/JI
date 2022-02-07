package ji.common.structures;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DictionaryValueTest {

	@Test
	@Parameters(method = "dataGetValue")
	public void testGetValue(Object value, Class<?> clazz, Object expected) {
		DictionaryValue dictionaryValue = new DictionaryValue(value);
		assertEquals(expected, dictionaryValue.getValue(clazz));
	}
	
	public Object[] dataGetValue() {
		return new Object[] {
			new Object[] {false, Boolean.class, false},
			new Object[] {false, boolean.class, false},
			new Object[] {"true", Boolean.class, true},
			new Object[] {"true", boolean.class, true},
			new Object[] {"on", boolean.class, true},
			new Object[] {"1", Boolean.class, true},
			new Object[] {42, int.class, 42},
			new Object[] {"42", int.class, 42},
			new Object[] {42, Integer.class, 42},
			new Object[] {"42", Integer.class, 42},
			new Object[] {42, double.class, 42.0},
			new Object[] {"42", double.class, 42.0},
			new Object[] {42, Double.class, 42.0},
			new Object[] {"42", Double.class, 42.0},
			/********/
			new Object[] {"true", Boolean.class, true},
			new Object[] {"42", Byte.class, (byte)42},
			new Object[] {"42", Short.class, (short)42},
			new Object[] {"42", Integer.class, 42},
			new Object[] {"42", Long.class, 42L},
			new Object[] {"42", Float.class, 42f},
			new Object[] {"42", Double.class, 42.0},
			new Object[] {"C", Character.class, 'C'},
			new Object[] {"C", char.class, 'C'},
			new Object[] {"string", String.class, "string"},
			/********/
			new Object[] {"null", String.class, "null"},
			new Object[] {null, String.class, null},
			/*******/
			new Object[] {
				new MapInit<>().append("a", "a").append("b", "b").toMap(),
				Map.class,
				new MapInit<>().append("a", "a").append("b", "b").toMap()
			},
			new Object[] {
				new MapInit<>().append("a", "a").append("b", "b").toMap(),
				MapDictionary.class,
				new MapDictionary<>(new MapInit<>().append("a", "a").append("b", "b").toMap())
			},
			new Object[] {
				new MapDictionary<>(new MapInit<>().append("a", "a").append("b", "b").toMap()),
				MapDictionary.class,
				new MapDictionary<>(new MapInit<>().append("a", "a").append("b", "b").toMap())
			},
			new Object[] {
				new MapDictionary<>(new MapInit<>().append("a", "a").append("b", "b").toMap()),
				Map.class,
				new MapInit<>().append("a", "a").append("b", "b").toMap()
			},
			/*************/
			new Object[] {
				Arrays.asList("a", 42),
				List.class,
				Arrays.asList("a", 42)
			},
			new Object[] {
				new ListDictionary<>(Arrays.asList("a", 42)),
				List.class,
				Arrays.asList("a", 42)
			},
			new Object[] {
				Arrays.asList("a", 42),
				ListDictionary.class,
				new ListDictionary<>(Arrays.asList("a", 42))
			},
			new Object[] {
				new ListDictionary<>(Arrays.asList("a", 42)),
				ListDictionary.class,
				new ListDictionary<>(Arrays.asList("a", 42))
			},
		};
	}
	
}
