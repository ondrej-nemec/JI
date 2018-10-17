package common;

import java.io.File;

import common.envenums.AppMode;
import common.envenums.SupportedOs;

public class Env {
	
	public final SupportedOs os;
	
	public final AppMode mode;
	
	public final String databaseUrlConnection;
	
	public final String databaseLogin;

	public final String databasePassword;
	
	public final String pathToLogs;
	
	public Env() {
		//TODO from file
		this.os = getOs();		
		this.mode = AppMode.PROD;		
		this.databaseUrlConnection = "";		
		this.databaseLogin = "";
		this.databasePassword = "";		
		this.pathToLogs = "logs";
	}
	
	protected SupportedOs getOs() {
		switch(File.separator) {
			case "/":
				return SupportedOs.LINUX;
			case "\\":
			default:
				return SupportedOs.WINDOWS;
		}
	}
}
