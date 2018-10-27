package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import common.env.AppMode;
import common.env.SupportedOs;

public class Env {
	
	public final AppMode mode;
	
	public final String pathToLogs;
	
	public final String pathToAppWorkspace;
	
	public final String databaseType;
	
	public final String databaseLocation;
	
	public final String databaseName;
	
	public final String databaseLogin;

	public final String databasePassword;
	
	public Env() {
		this.mode = AppMode.DEV;		
		this.databaseType = "";
		this.databaseLocation = "";
		this.databaseName = "";
		this.databaseLogin = "root";
		this.databasePassword = "";		
		this.pathToLogs = "workspace/logs/";
		this.pathToAppWorkspace = "workspace/";
	}
	
	public Env(
			String databaseType,
			String databaseLocation,
			String databaseName,
			String databseLogin,
			String databasePassword
	) {
		this.mode = AppMode.DEV;		
		this.databaseType = databaseType;	
		this.databaseLocation = databaseLocation;
		this.databaseName = databaseName;
		this.databaseLogin = databseLogin;
		this.databasePassword = databasePassword;
		this.pathToLogs = "workspace/logs/";
		this.pathToAppWorkspace = "workspace/";
	}
	
	public Env(
			String appName,
			AppMode appMode,
			String databaseType,
			String databaseLocation,
			String databaseName,
			String databseLogin,
			String databasePassword
	) {
		this.mode = appMode;		
		this.databaseType = databaseType;
		this.databaseLocation = databaseLocation;
		this.databaseName = databaseName;
		this.databaseLogin = databseLogin;
		this.databasePassword = databasePassword;
		
		String workspace = (
					Os.getOs() == SupportedOs.LINUX
					? System.getenv("HOME")
					: System.getenv("APPDATA")
				) + "/" + appName;
		this.pathToAppWorkspace = workspace + "/";
		this.pathToLogs = workspace + "/logs/";
	}

	public Env(Console console) {
		console.out("App Mode dev/prod (DEV):");
		String answer = console.in().toLowerCase();
		this.mode = answer.equals("prod") ? AppMode.PROD : AppMode.DEV;
		
		console.out("path to app workspace dir:");	
		this.pathToAppWorkspace = console.in();
		
		console.out("path to logs dir:");	
		this.pathToLogs = console.in();
		
		console.out("database type (mysql / derby supported):");
		this.databaseType = console.in();
		
		console.out("database location:");
		this.databaseLocation = console.in();
		
		console.out("database name url:");
		this.databaseName = console.in();
		
		console.out("database login:");		
		this.databaseLogin = console.in();
		
		console.out("database password:");
		this.databasePassword = console.in();
	}
	
	@Override
	public String toString() {
		return "AppMode: " + mode + "\n"
				+ "path to logs: " + pathToLogs + "\n"
				+ "path to app workspace: " + pathToAppWorkspace + "\n"
				+ "database type: " + databaseType + "\n"
				+ "database location: " + databaseLocation + "\n"
				+ "database name: " + databaseName + "\n"
				+ "database login: " + databaseLogin + "\n"
				+ "database password: " + databasePassword + "\n";
	}
	
	public void createAppDirs() throws IOException {
		initDir(pathToAppWorkspace);
		initDir(pathToLogs);
	}	
	
	private void initDir(String dirName) throws IOException {
		if (!Files.isDirectory(Paths.get(dirName)))
			Files.createDirectory(Paths.get(dirName));
	}
}
