package translator;

import java.util.List;

public class LanguageSettings {

	private final String defaultLang;
	
	private final List<Locale> locales;

	public LanguageSettings(String defaultLang, List<Locale> locales) {
		this.defaultLang = defaultLang;
		this.locales = locales;
	}

	public String getDefaultLang() {
		return defaultLang;
	}

	public List<Locale> getLocales() {
		return locales;
	}
	
}
