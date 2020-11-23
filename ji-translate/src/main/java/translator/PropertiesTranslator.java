package translator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import utils.io.PropertiesLoader;

public class PropertiesTranslator implements Translator {
	
	private final Logger logger;
	
	private final Map<Locale, Properties> resources;
	
	private final Locale locale;
	
	private final String[] files;
	
	public PropertiesTranslator(Logger logger, String... files) {
		this(logger, Locale.getDefault(), files);
	}
	
	public PropertiesTranslator(Logger logger, Locale locale, String... files) {
		this.locale = locale;
		this.logger = logger;
		this.resources = new HashMap<>();
		this.files = files;
		resources.put(locale, load(locale, files));
	}
	
	private Properties load(Locale locale, String[] files) {
		Properties messages = new Properties();
		for (String file : files) {
			String name = String.format("%s.%s.properties", file, locale);
			try {
				Properties prop = PropertiesLoader.loadProperties(name);
				messages.putAll(prop);
			} catch (IOException e) {
				logger.warn("Cannot load properies file: " + name);
				String name2 = String.format("%s.properties", file);
				try {
					Properties prop = PropertiesLoader.loadProperties(name2);
					messages.putAll(prop);
				} catch (IOException e1) {
					logger.error("Cannot load properies file: " + name2);
				}
			}
		}
		return messages;
	}

	@Override
	public String translate(String key) {
		return translate(key, locale);
	}

	@Override
	public String translate(String key, Locale locale) {
		if (resources.get(locale) == null) {
			load(locale, files);
		}
		String value = resources.get(locale).getProperty(key);
		if (value == null) {
			logger.warn("Missing translation: " + key);
			return key;
		}
		return value;
	}

	@Override
	public String translate(String key, Map<String, String> variables) {
		return translate(key, variables, locale);
	}

	@Override
	public String translate(String key, Map<String, String> variables, Locale locale) {
		String value = translate(key, locale);
		for (String varName : variables.keySet()) {
			value = value.replaceAll("\\%" + varName + "\\%", variables.get(varName));
		}
		return value;
	}

	

}
