package database;

import org.flywaydb.core.Flyway;

public class Migrate {

	public Migrate(final String url, final String login, final String password, final String migrationsLocation) {
		Flyway f = Flyway.configure().dataSource(url, login, password).locations(migrationsLocation).load();		
		f.migrate();	
	}
	
}
