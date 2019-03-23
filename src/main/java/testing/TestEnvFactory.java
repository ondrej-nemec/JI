package testing;

import common.Env;
import common.env.AppMode;

public class TestEnvFactory {

	public static Env createEnv() {
		try {
			return new Env(AppMode.TEST);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
