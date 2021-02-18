package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import utils.enums.AppMode;
import utils.io.PropertiesLoader;

public class Env implements Dictionary {
	
	private final Properties properties;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this.properties = PropertiesLoader.loadProperties(path); // loadProperties(path);
	}
	
	public Env(final Properties properties) {
		this.properties = properties;
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

	@Override
	public Object getValue(String name) {
		return properties.get(name);
	}

}
