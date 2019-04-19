package testing;

import java.util.Properties;

import utils.Env;
import utils.enums.AppMode;

public class TestCase {

	protected final Env env;
	
	public TestCase(final Properties properties) {
		this.env = new Env(AppMode.TEST, properties);
	}
	
	public TestCase(final String propertiesPath) {
		try {
			this.env = new Env(AppMode.TEST, propertiesPath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
