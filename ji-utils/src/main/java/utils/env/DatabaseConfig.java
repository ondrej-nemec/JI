package utils.env;


public class DatabaseConfig {

	public final String type;
	
	public final String pathOrUrlToLocation; // path is relative ..workspace/pathOrUrlToLocation
	
	public final String schemaName;
	
	public final String login;

	public final String password;
	
	public final String pathToMigrations;
	
	public final boolean runOnExternalServer;
	
	public final String timezone;
	
	public DatabaseConfig(
			final String type,
			final String pathOrUrlToLocation,
			final boolean runOnExternalServer,
			final String schemaName,
			final String login,
			final String password,
			final String pathToMigrations,
			final String timezone) {
		this.type = type;
		this.pathOrUrlToLocation = pathOrUrlToLocation;
		this.schemaName = schemaName;
		this.login = login;
		this.password = password;
		this.pathToMigrations = pathToMigrations;
		this.runOnExternalServer = runOnExternalServer;
		this.timezone = timezone;
	}

}
