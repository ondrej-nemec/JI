package translator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import common.Implode;
import common.Logger;

public class Translator {
	
	private final static String VARIABLE_SEPARATOR = "%";
	private final static String COUNT_SEPARATOR = "\\|";
	private final static String MESSAGES_SEPARATOR = ";";
	
	private final Logger logger;
	
	private final ResourceBundle defaultResource;
	
	private final Map<String, ResourceBundle> otherResources;
	
	public Translator(Logger logger, String pathToFiles, String defaultName, String... others) {
		this.logger = logger;
		this.defaultResource = loadBundle(pathToFiles + defaultName);
		this.otherResources = new HashMap<>();
		
		for (String other : others) {
			otherResources.put(other, loadBundle(pathToFiles + other));
		}
		otherResources.put(defaultName, defaultResource);
	}
	
	private ResourceBundle loadBundle(String path) {
		try {
			return ResourceBundle.getBundle(path);
		} catch(Exception e) {}
		try (InputStream fis = new FileInputStream(path + ".properties")) {
			return new PropertyResourceBundle(fis);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * key structure: <resource>.<key> OR <key>
	 * if <resource> not exists returns <resource.key> as <key> from default
	 * if no message for <key> founded, <key> returned 
	 * message structure: "some text"
	 */
	public String translate(String key) {
		return trans(key);
	}

	/**
	 * key structure: <resource>.<key> OR <key>
	 * if <resource> not exists returns <resource.key> as <key> from default
	 * if no message for <key> founded, <key> returned 
	 * message structure: "text %variable% %another-variable%"
	 */
	public String translate(String key, Map<String, String> variables) {
		String partialMessage = trans(key);
		return replaceVariableNamesWithValues(partialMessage, variables);
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
	public String translate(String key, int count, Map<String, String> variables) {
		String counts = trans(key);
		String partialMessage = selectMessageWithCount(counts, count);
		return replaceVariableNamesWithValues(partialMessage, variables);
	}

	public String translate(String key, int count) {
		return translate(key, count, new HashMap<>());
	}
	
	/*********** Plain translate ************/
	
	private String trans(String key) {
		String from = getFrom(key);
		if (from == null) {
			return trans(key, defaultResource);
		}
		String newKey = getKey(key);
		ResourceBundle bundle = otherResources.get(from);
		if (bundle != null) {
			return trans(newKey, bundle);
		}
		return trans(key, defaultResource);
	}
	
	private String trans(String key, ResourceBundle from) {
		try{
			return from.getString(key);
		}catch (MissingResourceException e){
			logger.warn("Missing key - " + key + " in " + from.getBaseBundleName());
			return key;
		}
	}
	
	/*********** Bundle loading ************/
	
	private String getFrom(String key) {
		String[] keys = key.split("\\.");
		if (keys.length > 1) {
			return keys[0];
		}
		return null;
	}
	
	private String getKey(String key) {
		String[] keys = key.split("\\.");
		if (keys.length < 2) {
			return key;
		}
		return Implode.implode(".", keys, 1, keys.length - 1);
	}
	
	/************** Replacing **************/
	
	private String replaceVariableNamesWithValues(String partialMessage, Map<String, String> variables) {
		String message = partialMessage;
		for (String name : variables.keySet()) {
			String value = variables.get(name);
			message = message.replaceAll(VARIABLE_SEPARATOR + name + VARIABLE_SEPARATOR, value);
		}
		return message;
	}
	
	private String selectMessageWithCount(String all, int count) {
		try {
			String[] alls = all.split(MESSAGES_SEPARATOR);
			String def = all + " (" + count + ")";
			for (String candidate : alls) {
				String[] messages = candidate.split(COUNT_SEPARATOR);
				if (messages[0].equals("D")) {
					def = messages[1];
				} else if (countMatch(count, messages[0])) {
					return messages[1];
				}
			}
			return def;
		} catch (Exception e) {
			logger.warn("Problem with parsing message " + all, e);
			return all;
		}
	}

	private boolean countMatch(int count, String string) {
		try {
			switch (string.charAt(0)) {
			case '<':
			case '(':
				return countMatch(string, count);
			case '[': return countMatch(string.replaceAll("\\]|\\[", "").split(","), count);
			default: return Integer.parseInt(string) == count;
			}
		} catch (Exception e) {
			logger.error("Trying check '" + string + "' for count : " + count, e);
			return false;
		}
	}

	private boolean countMatch(String string, int count) {
		boolean lowerInclude = string.charAt(0) == '<';
		boolean higherInclude = string.charAt(string.length() - 1) == '>';

		string = string.replace("I", Integer.MAX_VALUE + "").replaceAll("<|>|\\(|\\)", "");
		String[] strings = string.split(",");
		
		int lower = Integer.parseInt(strings[0]);
		int higher = Integer.parseInt(strings[1]);

		boolean lowerAllowed;
		if (lowerInclude) {
			lowerAllowed = lower <= count;
		} else {
			lowerAllowed = lower < count;
		}
		boolean higherAllowed;
		if (higherInclude) {
			higherAllowed = higher >= count;
		} else {
			higherAllowed = higher > count;
		}

		return lowerAllowed && higherAllowed;
	}

	private boolean countMatch(String[] split, int count) {
		for (String integer : split) {
			if (Integer.parseInt(integer) == count) {
				return true;
			}
		}
		return false;
	}

}
