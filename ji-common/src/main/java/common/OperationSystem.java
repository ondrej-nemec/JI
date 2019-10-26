package common;

import java.io.File;

public enum OperationSystem {
	
	NEW_LINE("\r\n", "\n"),
	PATH_SEPARATOR("\\", ""),
	PRE_COMMAND("cmd /c ", "/"),
	CLI_EXTENSION(".bat", ""); //.sh
	
	enum Supported {
		LINUX,
		WINDOWS
	}
	
	private final String linux;
	
	private final String windows;
	
	private OperationSystem(String windows, String linux) {
		this.linux = linux;
		this.windows = windows;
	}
	
	@Override
	public String toString() {
		Supported type = getActualOs();
		switch (type) {
		case LINUX: return linux;
		case WINDOWS: return windows;
		default:
			throw new RuntimeException("Unsupported operation system type: " + type);
		}
	}	
	
	public static Supported getActualOs() {
		switch(File.separator) {
		case "\n":
			return Supported.LINUX;
		case "\r\n":
		default:
			return Supported.WINDOWS;
		}
	}
	
}
