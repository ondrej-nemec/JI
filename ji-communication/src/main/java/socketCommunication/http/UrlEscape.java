package socketCommunication.http;

import static common.MapInit.t;

import common.structures.TwinList;

public class UrlEscape {

	private final static TwinList<String, String> ESCAPE = 
			TwinList.fromArray(
					t("%25", "%"),
					t("%3F", "\\?"),
					t("%2F", "/"),
					t("%5C", "\\\\"), // escaped \
					t("%3A", ":"),
					t("%2B", "\\+"),
					// t("\\+", " "), // cannot be here, must be escaped separatelly // escaped +
					t("%3D", "="),
					t("%26", "&"),
					t("%27", "'")/*,
					t("", "\""), // not escaped
					t("", "*")// not escaped */
			);
	
	public static String unEscapeText(String text) {
		text = text.replaceAll("\\+", " ");
		for (String key : ESCAPE.getFirsts()) {
			text = text.replaceAll(key, ESCAPE.getByFirst(key));
		}
		return text;
	}
	
	public static String escapeText(String text) {
		for (String key : ESCAPE.getSeconds()) {
			text = text.replaceAll(key, ESCAPE.getBySecond(key));
		}
		text = text.replaceAll(" ", "+");
		return text;
	}
	
}
