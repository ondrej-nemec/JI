package testing;

import java.util.Properties;

import utils.Env;

public class TestCase {

	protected final Env env;
	
	public TestCase(final Properties properties) {
		this.env = new Env(properties);
	}
	
	public TestCase(final String propertiesPath) {
		try {
			this.env = new Env(propertiesPath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
