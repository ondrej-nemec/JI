package common;

import java.util.Arrays;
import java.util.function.Function;

public class Implode {
	
	@SafeVarargs
	public static <T> String implode(String glue, T... data) {
		return implode((a)->a.toString(), glue, data);
	}

	@SafeVarargs
	public static <T> String implode(Function<T, String> toString, String glue, T... data) {
		return implode(toString, glue, Arrays.asList(data));
	}

	public static <T> String implode(String glue, Iterable<T> data) {
		return implode((a)->a.toString(), glue, data);
	}

	public static <T> String implode(Function<T, String> toString, String glue, Iterable<T> data) {
		StringBuilder result = new StringBuilder();
		for (T t : data) {
			if (!result.toString().isEmpty()) {
				result.append(glue);
			}
			result.append(toString.apply(t));
		}
		return result.toString();
	}
	
	/*
	@Deprecated
	public static <T> String implode(String glue, T[] data, int from, int to) {
		return implode(glue, data, from, to, (a)->a.toString());
	}

	@Deprecated
	public static <T> String implode(String glue, T[] data, int from, int to, Function<T, String> toString) {
		if (to >= data.length || from < 0 || from > to) {
			throw new LogicException(String.format(
					"Incorrect values of from (%d), to (%d) in relation with length of array (%d)",
					from, to, data.length
				));
		}
		StringBuilder result = new StringBuilder();
		for (int i = from; i <= to; i++) {
			if (i != from) {
				result.append(glue);
			}
			result.append(toString.apply(data[i]));
		}
		return result.toString();
	}
	*/
}
