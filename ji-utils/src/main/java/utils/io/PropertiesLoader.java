package utils.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesLoader {
	
	public static Properties loadProperties(final String path) throws IOException {
		return loadProperties(path, "utf-8");
	}

	public static Properties loadProperties(final String path, String charset) throws IOException {
		Properties prop = new Properties();
		try (InputStream is = prop.getClass().getResourceAsStream("/" + path)) {
			prop.load(new InputStreamReader(is, charset));
			return prop;
		} catch (Exception e) {/* ignored */}
		try(InputStream is = new FileInputStream(path)) {
			prop.load(new InputStreamReader(is, charset));
			return prop;
		} catch (Exception e) { /* ignored */}
		throw new FileNotFoundException(path);
	}
	
}
