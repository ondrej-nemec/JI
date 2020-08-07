package socketCommunication.http;

import static common.MapInit.t;

import common.structures.TwinList;

public class UrlEscape {

	private final static TwinList<String, String> ESCAPE = 
			// TODO https://www.w3schools.com/tags/ref_urlencode.ASP
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
					t("%27", "'"),
					t("%7B", "\\{"),
					t("%7D", "\\}"),
					t("%5B", "\\["),
					t("%5D", "\\]"),
					t("%3C", "<"),
					t("%3E", ">"),
					t("%22", "\"")
					
					/*,
					t("", "\""), // not escaped
					t("", "*")// not escaped */
			);
	
	// http://localhost:10123/aaa/bbb?hidden1=hiden_value&text1=%7Ba%7Dbcde&submit1=Send
		
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
