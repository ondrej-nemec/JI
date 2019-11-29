package common;

import java.io.File;

public enum OperationSystem {
	
/*
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static void main(String[] args) {
		System.out.println(OS);
		if (isWindows()) {
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}
*/
	
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
