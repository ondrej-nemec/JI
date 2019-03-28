package testing;

import java.util.Properties;

import common.Env;
import common.env.AppMode;

abstract public class TestCase {

	protected final Env env;
	
	public TestCase() {
		Properties prop = getProperties();
		
		if(prop == null) {
			this.env = TestEnvFactory.createEnv();
		} else {
			this.env = new Env(AppMode.TEST, prop);
		}	
		
	}
	
	protected abstract Properties getProperties();
	
}
