package common;

import common.env.AppMode;
import common.env.SupportedOs;

public class Env {
	
	public final AppMode mode;
	
	public final String appName;
	
	public final String pathToLogs;
	
	public final String pathToAppWorkspace;
	
	public Env() {
		this.mode = AppMode.DEV;
		this.appName = "App name";
		this.pathToLogs = "workspace/logs/";
		this.pathToAppWorkspace = "workspace/";
	}
	
	public Env(final String appName, final AppMode appMode) {
		this.mode = appMode;
		this.appName = appName;
		if (appMode == AppMode.DEV) {
			this.pathToLogs = "workspace/logs/";
			this.pathToAppWorkspace = "workspace/";
		} else {
			String workspace = (
						Os.getOs() == SupportedOs.LINUX
						? System.getenv("HOME")
						: System.getenv("APPDATA")
					) + "/" + appName;
			this.pathToAppWorkspace = workspace + "/";
			this.pathToLogs = workspace + "/logs/";
		}	
	}
}
