package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import common.Os;
import common.emuns.SupportedOs;
import utils.env.DatabaseConfig;
import utils.env.LoggerConfig;
import utils.enums.AppMode;

public class Env {

	public final AppMode mode;
	
	private final Properties properties;
		
	public Env(final String path) throws FileNotFoundException, IOException {
		this.properties = loadProperties(path);
		this.mode = selectMode(properties.getProperty("app.mode"));
	}
	
	public Env(final Properties properties) {
		this.properties = properties;
		this.mode = selectMode(properties.getProperty("app.mode"));
	}

	public String getProperty(final String key) {
		switch(key) {
			case "APPDATA": return generateAppData() + getProp("app.name") + "/";
			default: return getProp(key);
		}		
	}
	
	private String getProp(final String key) {
		String property = properties.getProperty(key);
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
	
	public LoggerConfig createLogConfig() {
		return new LoggerConfig(
				mode,
				getProperty("log.type"),
				getProperty("log.logFile")
		);
	}
	
	protected Properties getProperties() {
		return properties;
	}
	
	private String generateAppData() {
		return Os.getOs() == SupportedOs.LINUX
				? System.getenv("HOME")
				: System.getenv("APPDATA")
			 + "/";
	}
	
	private Properties loadProperties(final String path) throws IOException {
		Properties prop = new Properties();
		InputStream is = getClass().getResourceAsStream(path);
		if (is == null)
			throw new FileNotFoundException(path);
		prop.load(is);
		return prop;
	}
	
	private AppMode selectMode(String mode) {
		if (mode == null)
			throw new RuntimeException("App mode is null");
		switch (mode.toLowerCase()) {
		case "test": return AppMode.TEST;
		case "dev": return AppMode.DEV;
		case "prod": return AppMode.PROD;
		default:
			throw new RuntimeException("Unsupported app mode: " + mode);
		}
	}
}
