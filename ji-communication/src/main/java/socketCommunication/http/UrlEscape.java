package socketCommunication.http;

import static common.structures.MapInit.t;

import common.structures.UniqueMap;

public class UrlEscape {

	private final static UniqueMap<String, String> ESCAPE = 
			// https://www.w3schools.com/tags/ref_urlencode.ASP
			UniqueMap.fromArray(
					t("%25", "%"), // must be first
					t("%3F", "\\?"),
					t("%2F", "/"),
					t("%5C", "\\\\"), // escaped \
					t("%3A", ":"),
					t("%2B", "\\+"),
					// t("\\+", " "), // cannot be here, must be escaped separatelly // escaped +
					t("%3D", "="),
					t("%26", "&"),
					t("%27", "'"),
					t("%7B", "\\{"),
					t("%7D", "\\}"),
					t("%5B", "\\["),
					t("%5D", "\\]"),
					t("%3C", "<"),
					t("%3E", ">"),
					t("%22", "\""),
					t("%2C", ","),
					t("%20", " "),
					t("%40", "@"),
					t("%23", "#"),
					t("%3B", ";")
					//t("%21", "!")
					//t("%24", "\\$")
					/*,
					t("", "\""), // not escaped
					t("", "*")// not escaped */
			);
	
	// http://localhost:10123/aaa/bbb?hidden1=hiden_value&text1=%7Ba%7Dbcde&submit1=Send

	/**
	 * Replaced by https://www.baeldung.com/java-url-encoding-decoding
	 * @param text
	 * @return
	 */
	@Deprecated
	public static String unEscapeText(String text) {
		text = text.replaceAll("\\+", " ");
		for (String key : ESCAPE.keySetA()) {
			text = text.replaceAll(key, ESCAPE.getA(key));
		}
		return text;
	}
	
	/**
	 * Replaced by https://www.baeldung.com/java-url-encoding-decoding
	 * @param text
	 * @return
	 */
	@Deprecated
	public static String escapeText(String text) {
		for (String key : ESCAPE.keySetB()) {
			text = text.replaceAll(key, ESCAPE.getB(key));
		}
		text = text.replaceAll(" ", "+");
		return text;
	}
	
}
