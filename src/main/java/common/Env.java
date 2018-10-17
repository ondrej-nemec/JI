package common;

import common.env.AppMode;

public class Env {
	//TODO app data or some else free space for writing
	
	public final AppMode mode;
	
	public final String databaseUrlConnection;
	
	public final String databaseLogin;

	public final String databasePassword;
	
	public final String pathToLogs;
	
	public Env() {
		this.mode = AppMode.DEV;		
		this.databaseUrlConnection = "";		
		this.databaseLogin = "";
		this.databasePassword = "";		
		this.pathToLogs = "logs";
	}
	
	public Env(Console console) {
		console.out("App Mode dev/prod (DEV):");
		String answer = console.in().toLowerCase();
		this.mode = answer.equals("prod") ? AppMode.PROD : AppMode.DEV;
		
		console.out("database path to logs dir:");	
		this.pathToLogs = console.in();
		
		console.out("database connection url:");
		this.databaseUrlConnection = console.in();
		
		console.out("database login:");		
		this.databaseLogin = console.in();
		
		console.out("database password:");
		this.databasePassword = console.in();
	}
	
	@Override
	public String toString() {
		return "AppMode: " + mode + "\n"
				+ "path to logs: " + pathToLogs + "\n"
				+ "database connection url: " + databaseUrlConnection + "\n"
				+ "database login: " + databaseLogin + "\n"
				+ "database password: " + databasePassword + "\n";
	}
}
