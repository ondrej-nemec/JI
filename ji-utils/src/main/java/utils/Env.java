package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import utils.enums.AppMode;

public class Env {

	public final AppMode mode;
	
	private final Properties properties;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this.properties = loadProperties(path);
		this.mode = selectMode(properties.getProperty("app.mode"));
	}
	
	public Env(final Properties properties) {
		this.properties = properties;
		this.mode = selectMode(properties.getProperty("app.mode"));
	}

	public String getProperty(final String key) {
		String property = properties.getProperty(key);
		if (property == null)
			throw new RuntimeException("Property was not found. Key: " + key + ", in " + mode + " mode.");
		return property;		
	}

	protected Properties getProperties() {
		return properties;
	}

	private Properties loadProperties(final String path) throws IOException {
		Properties prop = new Properties();
		
		try (InputStream is = getClass().getResourceAsStream("/" + path)) {
			prop.load(is);
			return prop;
		} catch (Exception e) {/* ignored */}
		try(InputStream is = new FileInputStream(path)) {
			prop.load(is);
			return prop;
		} catch (Exception e) { /* ignored */}
		
		throw new FileNotFoundException(path);
	}
	
	private AppMode selectMode(String mode) {
		if (mode == null)
			throw new RuntimeException("App mode is null");
		return AppMode.valueOf(mode.toUpperCase());
	}
}
