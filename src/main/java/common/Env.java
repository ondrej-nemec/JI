package common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import common.env.AppMode;
import common.env.SupportedOs;
import logging.Logger;

public class Env {
	/*
	// TODO maybe sometimes
	public static Env loadFromFile(final AppMode mode, final String path) {
		return null;
	}
	
	public static Env loadFromProperties(final AppMode mode, final Properties properties) {
		return null;
	}
	*/
	public final AppMode mode;
	
	private final Properties properties;
		
	public Env(final AppMode mode, final String path) throws FileNotFoundException, IOException {
		Properties prop =  new Properties();
		
		if (mode == AppMode.AUTOLOAD) {
			this.mode = loadProperties(path, prop);
		} else {
			this.mode = mode;
			loadProperties(path, prop, mode);
		}

		this.properties = prop;
		
		Logger.setEnvIfNotSetted(this);
	}
	
	public Env(final AppMode mode, final Properties properties) {
		if (mode == AppMode.AUTOLOAD)
			throw new RuntimeException("Autoload is not supported as mode for code constructor.");
		this.mode = mode;
		this.properties = properties;
		Logger.setEnvIfNotSetted(this);
	}
	
	public String getProperty(final String key) {
		switch(key) {
			case "APPDATA": return generateAppData() + getProp("app.name") + "/";
			default: return getProp(key);
		}		
	}
	
	private String getProp(final String key) {
		String property = getProperty(key);
		if (property == null)
			throw new RuntimeException("Property was not found. Key: " + key + ", in " + mode + " mode.");
		return property;
	}
	
	@Deprecated
	public String getPropertyOrThrowIfNotExists(final String key) {
		return getProperty(key);
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
	
	protected Properties getProperties() {
		return properties;
	}
	
	private String generateAppData() {
		return Os.getOs() == SupportedOs.LINUX
				? System.getenv("HOME")
				: System.getenv("APPDATA")
			 + "/";
	}

	private String getPropertiesPath(final String path, final AppMode mode) {
		return path + "/env." + mode.toString().toLowerCase() + ".properties";
	}
	
	private AppMode loadProperties(final String path, final Properties prop) throws IOException {
		try {
			loadProperties(path, prop, AppMode.PROD);
			return AppMode.PROD;
		} catch(FileNotFoundException e) { /*ignored*/ }
		try {
			loadProperties(path, prop, AppMode.BETA);
			return AppMode.BETA;
		} catch(FileNotFoundException e) { /*ignored*/ }
		try {
			loadProperties(path, prop, AppMode.DEV);
			return AppMode.DEV;
		} catch(FileNotFoundException e) { /*ignored*/ }
		try {
			loadProperties(path, prop, AppMode.TEST);
			return AppMode.TEST;
		} catch(FileNotFoundException e) { /*ignored*/ }
		
		throw new IOException("No properties file was founded.");
	}
	
	private void loadProperties(final String path, final Properties prop, final AppMode mode) throws IOException {
		String name = getPropertiesPath(path, mode);
		InputStream is = getClass().getResourceAsStream(name);
		if (is == null)
			throw new FileNotFoundException(name);
		prop.load(is);
	}
}
