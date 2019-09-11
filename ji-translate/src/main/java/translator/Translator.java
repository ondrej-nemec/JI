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
import common.exceptions.NotImplementedYet;

public class Translator {
	
	private final static String VARIABLE_START = "%";
	private final static String VARIABLE_END = "%";
	
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
	 * key structure: resource.key
	 * if "resource" not exists returns resource.key from default
	 */
	public String translate(String key) {
		return trans(key);
	}

	/**
	 * key structure: resource.key
	 * if "resource" not exists returns resource.key from default
	 */
	public String translate(String key, Map<String, String> variables) {
		String partialMessage = trans(key);
		return replaceVariableNamesWithValues(partialMessage, variables);
	}

	/**
	 * key structure: resource.key
	 * if "resource" not exists returns resource.key from default
	 */
	public String translate(String key, int count, Object... variables) {
		throw new NotImplementedYet();
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
			message = message.replaceAll(VARIABLE_START + name + VARIABLE_END, value);
		}
		return message;
	}

}
