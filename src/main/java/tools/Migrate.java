package tools;

import org.flywaydb.core.Flyway;

public class Migrate {

	public Migrate(String url, String login, String password) {
		Flyway f = new Flyway();
		f.setDataSource(url, login, password);
		f.setLocations("migrations");
		f.migrate();	
	}
	
	public static void main(String[] args) {
		new Migrate(args[0], args[1], args[2]);
	}
}
