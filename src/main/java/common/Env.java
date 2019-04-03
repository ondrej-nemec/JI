package common;

import java.io.File;
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
		this.properties = new Properties();
		
		if (mode == AppMode.AUTOLOAD) {
			if (existsProperties(AppMode.PROD)) {
				this.mode = AppMode.PROD;
			} else if (existsProperties(AppMode.DEV)) {
				this.mode = AppMode.DEV;
			} else if (existsProperties(AppMode.TEST)) {
				this.mode = AppMode.TEST;
			} else {
				throw new IOException("No properties file was founded.");
			}
		} else {
			this.mode = mode;
		}
		
		loadProperties();
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
	
	public String getPropertyOrThrowIfNotExist(final String key) {
		String property = getProperty(key);
		if (property == null)
			throw new RuntimeException("Property was not found. Key: " + key + ", in " + mode + " mode.");
		return property;
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
	
	private boolean existsProperties(final AppMode mode) {
		File f = new File(getPropertiesPath(mode));
		return f.exists() && f.isFile();
	}
	
	private String getPropertiesPath(final AppMode mode) {
		return "/env." + mode.toString().toLowerCase() + ".properties";
	}
	
	private void loadProperties() throws IOException {
		properties.load(getClass().getResourceAsStream(getPropertiesPath(mode)));
	}
}
