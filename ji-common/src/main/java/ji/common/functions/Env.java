package ji.common.functions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ji.common.structures.Dictionary;

public class Env implements Dictionary<String> {
	
	private final Properties properties;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this.properties = PropertiesLoader.loadProperties(path); // loadProperties(path);
	}
	
	public Env(final Properties properties) {
		this.properties = properties;
	}

	protected Properties getProperties() {
		return properties;
	}

	@Override
	public Object getValue(String name) {
		return properties.get(name);
	}

	@Override
	public void clear() {
		properties.clear();
	}

}
