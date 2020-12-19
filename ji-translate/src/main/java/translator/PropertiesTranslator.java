package translator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import utils.io.PropertiesLoader;

public class PropertiesTranslator implements Translator {
		
	private final Logger logger;
	
	private final Map<String, Properties> resources;
	
	private final Map<String, String> files;
	
	public PropertiesTranslator(Logger logger, String... files) {
		this.logger = logger;
		this.resources = new HashMap<>();
		this.files = new HashMap<>();
		for (String file : files) {
			this.files.put(new File(file).getName(), file);
		}
	}

	@Override
	public String translate(String key, Map<String, String> variables, Locale locale) {
		String value = trans(key, locale);
		for (String varName : variables.keySet()) {
			value = value.replaceAll("\\%" + varName + "\\%", variables.get(varName));
		}
		return value;
	}

	private String trans(String key, Locale locale) {
		String[] split = key.split("\\.", 2);
		
		Properties prop = null;
		if (split.length > 1) {
			prop = load(locale, split[0]);
			if (prop.isEmpty()) {
				prop = load(locale, "messages");
			} else {
				key = split[1];
			}
		}
		if (prop == null) {
			prop = load(locale, "messages");
		}	
		
		String value= prop.getProperty(key);
		if (value == null) {
			logger.warn("Missing translation: " + key);
			return key;
		}
		return value;
	}
	
	private Properties load(Locale locale, String module) {
		String localeKey = locale + "--" + module;
		if (resources.get(localeKey) != null) {
			return resources.get(localeKey);
		}
		logger.debug("No properties for: Locale=" + locale + ", module=" + module + ". Trying load.");
		
		Properties messages = new Properties();
		String path = files.get(module);
		if (path == null) {
			logger.warn("No path for module " + module);
			return messages;
		}
		String name = String.format("%s.%s.properties", path, locale);
		try {
			Properties prop = PropertiesLoader.loadProperties(name);
			messages.putAll(prop);
		} catch (IOException e) {
			logger.warn("Cannot load properies file: " + name);
			String name2 = String.format("%s.properties", path);
			try {
				Properties prop = PropertiesLoader.loadProperties(name2);
				messages.putAll(prop);
			} catch (IOException e1) {
				logger.error("Cannot load properies file: " + name2);
			}
		}
		resources.put(localeKey, messages);
		return messages;
	}

}
