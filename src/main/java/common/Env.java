package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import common.env.AppMode;
import common.env.SupportedOs;
import logging.Logger;

public class Env {
	
	public final AppMode mode;
	
	private final Properties properties;
		
	public Env(final AppMode mode) throws FileNotFoundException, IOException {
		this.mode = mode;
		this.properties = new Properties();
		properties.load(getClass().getResourceAsStream("env.\" + mode.toString().toLowerCase() + \".properties"));
		Logger.setEnvIfNotSetted(this);
	}
	
	public Env(final AppMode mode, final Properties properties) {
		this.mode = mode;
		this.properties = properties;
		Logger.setEnvIfNotSetted(this);
	}
	
	public String getProperty(final String key) {
		switch(key) {
			case "APPDATA": return generateAppData() + properties.getProperty("app.name") + "/";
			default: return properties.getProperty(key);
		}		
	}
	
	public DatabaseConfig createDbConfig() {
		return new DatabaseConfig(
				getProperty("db.type"),
				getProperty("db.pathOrUrl"),
				getProperty("db.externalServer").equals("1") ? true : false,
				getProperty("db.schema"),
				getProperty("db.login"),
				getProperty("db.password"),
				getProperty("db.pathToMigrations")
		);
	}
	
	private String generateAppData() {
		return Os.getOs() == SupportedOs.LINUX
				? System.getenv("HOME")
				: System.getenv("APPDATA")
			 + "/";
	}
}
