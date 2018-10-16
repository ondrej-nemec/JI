package migrationtool;

import org.flywaydb.core.Flyway;

import common.Env;

public class Migrate {

	public Migrate(String url, String login, String password) {
		Flyway f = new Flyway();
		f.setDataSource(url, login, password);
		f.setLocations("migrations");
		f.migrate();	
	}
	
	public static void main(String[] args) {
		//TODO static provider of env
		Env env = new Env();
		new Migrate(env.databaseUrlConnection, env.databaseLogin, env.databasePassword);
	}
}
