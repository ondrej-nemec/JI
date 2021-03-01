package common;

import java.util.Arrays;
import java.util.function.Function;

public class Implode {
	
	@SafeVarargs
	public static <T> String implode(String glue, T... data) {
		return implode((a)->a==null?null:a.toString(), glue, data);
	}

	@SafeVarargs
	public static <T> String implode(Function<T, String> toString, String glue, T... data) {
		return implode(toString, glue, Arrays.asList(data));
	}

	public static <T> String implode(String glue, Iterable<T> data) {
		return implode((a)->a==null?null:a.toString(), glue, data);
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
}
