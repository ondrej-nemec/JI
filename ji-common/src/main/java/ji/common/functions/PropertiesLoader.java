package ji.common.functions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import ji.common.functions.InputStreamLoader;

public class PropertiesLoader {
	
	public static Properties loadProperties(final String path) throws IOException {
		return loadProperties(path, "utf-8");
	}

	public static Properties loadProperties(final String path, String charset) throws IOException {
		Properties prop = new Properties();
		prop.load(new InputStreamReader(InputStreamLoader.createInputStream(prop.getClass(), path), charset));
		return prop;
	}
	
}
