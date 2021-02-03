package common;

import java.util.function.Function;

import common.exceptions.LogicException;

public class Implode {

	@SafeVarargs
	public static <T> String implode(String glue, T... data) {
		return implode((a)->a.toString(), glue, data);
	}

	@SafeVarargs
	public static <T> String implode(Function<T, String> toString, String glue, T... data) {
		return implode(glue, data, 0, data.length - 1, toString);
	}

	/**
	 * 
	 * @param glue
	 * @param data
	 * @param from inclusive
	 * @param to inclusive
	 * @return
	 */
	public static <T> String implode(String glue, T[] data, int from, int to) {
		return implode(glue, data, from, to, (a)->a.toString());
	}

	public static <T> String implode(String glue, T[] data, int from, int to, Function<T, String> toString) {
		String result = "";
		if (to >= data.length || from < 0 || from > to) {
			throw new LogicException(String.format(
					"Incorrect values of from (%d), to (%d) in relation with length of array (%d)",
					from, to, data.length
				));
		}
		for (int i = from; i <= to; i++) {
			if (i != from) {
				result += glue;
			}
			result += toString.apply(data[i]);
		}
		return result;
	}
	
}
