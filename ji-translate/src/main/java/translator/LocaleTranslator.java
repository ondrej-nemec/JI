package translator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import common.functions.PropertiesLoader;

public class LocaleTranslator implements Translator {
	
	private final Logger logger;

	private Locale selectedLang;
	
	private final Map<String, Map<String, Properties>> resources;
	
	private final List<String> paths;
	
	private final Map<String, Locale> substitution;
	
	private final LanguageSettings settings;
	
	public LocaleTranslator(LanguageSettings settings, List<String> paths, Logger logger) {
		this(settings.getDefaultLang(), settings, paths, logger);
	}
	
	public LocaleTranslator(String selectedLang, LanguageSettings settings, List<String> paths, Logger logger) {
		this.logger = logger;
		this.paths = paths;
		this.resources = new HashMap<>();
		this.substitution = new HashMap<>();
		this.settings = settings;
		settings.getLocales().forEach((locale)->{
			substitution.put(locale.getLang(), locale);
			locale.getSubstitution().forEach((subst)->{
				substitution.put(subst, locale);
			});
		});
		if (substitution.get(selectedLang) == null) {
			if (settings.getLocales().size() == 0) {
				this.selectedLang = new Locale(selectedLang, true, Arrays.asList());
			} else {
				this.selectedLang = settings.getLocales().get(0);
			}
		} else {
			this.selectedLang = substitution.get(selectedLang);
		}
	}
	
	@Override
	public Locale getLocale() {
		return selectedLang;
	}

	@Override
	public Translator withLocale(String locale) {
		return new LocaleTranslator(locale, settings, paths, logger);
	}

	@Override
	public void setLocale(String locale) {
		this.selectedLang = substitution.get(locale);
	}
	
	public String translate(String key, Map<String, Object> variables, String locale) {
		String[] split = key.split("\\.", 2);
		try {
			String module = split[0];
			String transKey = split[1];
			Properties prop = getProperties(locale, module);
			String value = prop.getProperty(transKey);
			if (value == null) {
				logger.warn(String.format(
					"Missing translation [%s] in %s: %s (%s)",
					locale, module, transKey, variables
				));
				value = key;
				prop.put(transKey, value);
			}
			return value;
		} catch (Exception e) {
			logger.fatal("Problem with translating " + key, e);
			return key;
		}
	}
	
	private Properties getProperties(String locale, String domain) {
		Locale resourceLocale = substitution.get(locale);
		String resourceKey;
		if (resourceLocale == null) {
			logger.info("Given locale not found (including substitutions): " + locale + ", default used");
			resourceKey = settings.getDefaultLang();
		} else {
			resourceKey = resourceLocale.getLang();
		}
	
		Map<String, Properties> domainsResource = resources.get(resourceKey);
		if (domainsResource == null) {
			logger.info("Resources for locale not loaded yet. Loading " + resourceKey);
			domainsResource = new HashMap<>();
			resources.put(resourceKey, domainsResource);
		}
		
		Properties resource = domainsResource.get(domain);
		if (resource == null) {
			logger.info("No properties for domain: " + domain + ". Loading");
			resource = loadDomainResource(locale, domain);
			domainsResource.put(domain, resource);
		}
		
		return resource;
	}

	private Properties loadDomainResource(String locale, String domain) {
		Properties moduleProperties = new Properties();
		for(String path : paths) {
			String name = String.format(
				"%s%s%s.properties",
				path.endsWith("/") || path.endsWith("\\") ? path : path + "/",
				domain,
				locale.isEmpty() ? locale : String.format(".%s", locale)
			);
			try {
				Properties prop = PropertiesLoader.loadProperties(name);
				moduleProperties.putAll(prop);
			} catch (IOException e) {
				logger.warn("Cannot load properies file: " + name);
			}
		}
		return moduleProperties;
	}
	
}
