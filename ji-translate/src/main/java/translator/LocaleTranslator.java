package translator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import common.Logger;
import common.functions.PropertiesLoader;

public class LocaleTranslator implements Translator {
	
	private final Logger logger;

	private String selectedLang;
	
	private final String defaultLang;
	
	private final Map<String, Map<String, Properties>> resources;
	
	private final List<String> paths;
	
	private final Map<String, String> substitution;
	
	private final LanguageSettings settings;
	
	public LocaleTranslator(LanguageSettings settings, List<String> paths, Logger logger) {
		this(settings.getDefaultLang(), settings, paths, logger);
	}
	
	public LocaleTranslator(String selectedLang, LanguageSettings settings, List<String> paths, Logger logger) {
		this.selectedLang = settings.getDefaultLang();
		this.logger = logger;
		this.paths = paths;
		this.resources = new HashMap<>();
		this.substitution = new HashMap<>();
		this.defaultLang = selectedLang;
		this.settings = settings;
		settings.getLocales().forEach((locale)->{
			substitution.put(locale.getLang(), locale.getLang());
			locale.getSubstitution().forEach((subst)->{
				substitution.put(subst, locale.getLang());
			});
		});
	}
	
	@Override
	public String getLocale() {
		return selectedLang;
	}

	@Override
	public Translator withLocale(String locale) {
		return new LocaleTranslator(locale, settings, paths, logger);
	}

	@Override
	public void setLocale(String locale) {
		this.selectedLang = locale;
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
		String resourceKey = substitution.get(locale);
		if (resourceKey == null) {
			logger.info("Given locale not found (including substitutions): " + resourceKey + ", default used");
			resourceKey = defaultLang;
		}
		
		Map<String, Properties> domainsResource = resources.get(resourceKey);
		if (domainsResource == null) {
			logger.info("Resources for locale not loaded yet. Loading " + resourceKey);
			domainsResource = loadDomainResource(locale, domain);
			resources.put(resourceKey, domainsResource);
		}
		
		Properties resource = domainsResource.get(domain);
		if (resource == null) {
			logger.warn("No properties for domain: " + domain);
			resource = new Properties();
			domainsResource.put(domain, resource);
		}
		return resource;
	}

	private Map<String, Properties> loadDomainResource(String locale, String domain) {
		Map<String, Properties> domainsResource = new HashMap<>();
		for(String path : paths) {
			String name = String.format(
				"%s%s%s.properties",
				path.endsWith("/") || path.endsWith("\\") ? path : path + "/",
				domain,
				locale.isEmpty() ? locale : String.format(".%s", locale)
			);
			try {
				Properties moduleProperties = PropertiesLoader.loadProperties(name);
				if (!domainsResource.containsKey(domain)) {
					domainsResource.put(domain, new Properties());
				}
				domainsResource.get(domain).putAll(moduleProperties);
			} catch (IOException e) {
				logger.warn("Cannot load properies file: " + name);
			}
		}
		return domainsResource;
	}
	
}
