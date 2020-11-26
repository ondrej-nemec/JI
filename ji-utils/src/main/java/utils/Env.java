package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import utils.enums.AppMode;
import utils.io.PropertiesLoader;

public class Env {
	
	private final Properties properties;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this.properties = PropertiesLoader.loadProperties(path); // loadProperties(path);
	}
	
	public Env(final Properties properties) {
		this.properties = properties;
	}

	@Deprecated
	public String getProperty(final String key) {
		String property = properties.getProperty(key);
		if (property == null)
			throw new RuntimeException("Property was not found. Key: " + key);
		return property;		
	}
	
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	public Integer getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}
	
	public Double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}
	
	public List<String> getList(String key, String delimiter) {
		return Arrays.asList(getString(key).split(delimiter));
	}
	
	public AppMode getAppMode() {
		String mode = properties.getProperty("app.mode");
		if (mode == null)
			throw new RuntimeException("App mode is null");
		return AppMode.valueOf(mode.toUpperCase());
	}

	protected Properties getProperties() {
		return properties;
	}

}
