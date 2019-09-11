package common;

import common.exceptions.LogicException;

public class Implode {

	public static String implode(String glue, Object... data) {
		return implode(glue, data, 0, data.length - 1);
	}
	
	/**
	 * 
	 * @param glue
	 * @param data
	 * @param from inclusive
	 * @param to inclusive
	 * @return
	 */
	public static String implode(String glue, Object[] data, int from, int to) {
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
			result += data[i];
		}
		return result;
	}
}
