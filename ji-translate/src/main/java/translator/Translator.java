package translator;

import java.util.HashMap;
import java.util.Map;

public interface Translator {

	default String translate(String key) {
		return translate(key, new HashMap<>(), getLocale().getLang());
	}
	
	default String translate(String key, String locale) {
		return translate(key, new HashMap<>(), locale);
	}

	default String translate(String key, Map<String, Object> variables) {
		return translate(key, variables, getLocale().getLang());
	}
	
	Locale getLocale();
	
	Locale getLocale(String locale);
	
	/**
	 * key structure: <resource>.<key> OR <key>
	 * if <resource> not exists returns <resource.key> as <key> from default
	 * if no message for <key> founded, <key> returned 
	 * message structure: "text %variable% %another-variable%"
	 */
	String translate(String key, Map<String, Object> variables, String locale);
	
	Translator withLocale(Locale locale);
	
	default Translator withLocale(String locale) {
		return withLocale(getLocale(locale));
	}
	
	void setLocale(Locale locale);
	
	default void setLocale(String locale) {
		setLocale(getLocale(locale));
	}

	/**
	 * key structure: <resource>.<key> OR <key>
	 * if <resource> not exists returns <resource.key> as <key> from default
	 * if no message for <key> founded, <key> returned 
	 * message structure:
	 *   [<countResolution>]|"text %variable% %another-variable%";[<countResolution>]|"text %variable% %another-variable%"
	 *   <countResolution>: m,n - number, I - infinity
	 *   	n - exactly this number
	 *      [n,m] - in given list
	 *      (n,m) - in sequence from n to m exclusive
	 *      <n,m> - in sequence from n to m inclusive
	 *      (-I,n) - everything less n
	 *      (-I,n> - everything less or equals n
	 *      (m,I) - everything more that m
	 *      <m,I) - everything more or equals m
	 *      D - default - used if no other resolution matched, if default missing <key>.count returned 
	 *    <countCode> reffer to <resource>.<key>.<countCode> - there is final message 
	 */
//	String translate(String key, int count, Map<String, String> variables);

//	String translate(String key, int count);

}
