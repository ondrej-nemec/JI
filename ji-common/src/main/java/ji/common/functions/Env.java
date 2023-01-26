package ji.common.functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ji.common.structures.Dictionary;

public class Env implements Dictionary<String> {
	
	private final Properties properties;
	private final String key;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this(PropertiesLoader.loadProperties(path), null); // loadProperties(path);
	}
	
	public Env(final Properties properties) {
		this(properties, null);
	}
	
	private Env(Properties properties, String key) {
		this.properties = properties;
		this.key = key;
	}

	protected Properties getProperties() {
		return properties;
	}

	@Override
	public Object getValue(String name) {
		if (key == null) {
			return properties.get(name);
		}
		return properties.get(key + "." + name);
	}

	@Override
	public void clear() {
		properties.clear();
	}
	
	public Env getModule(String key) {
		return new Env(properties, (this.key == null ? "" : this.key + ".") + key);
	}

	@Override
	public String toString() {
		return key + ": " +  properties.toString();
	}
	
}
