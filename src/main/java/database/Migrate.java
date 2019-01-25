package database;

import org.flywaydb.core.Flyway;

public class Migrate {

	public Migrate(final String url, final String login, final String password, final String migrationsLocation) {
		Flyway f = new Flyway();
		f.setDataSource(url, login, password);
		f.setLocations(migrationsLocation);
		f.migrate();	
	}
	
}
