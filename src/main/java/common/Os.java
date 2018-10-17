package common;

import java.io.File;

import common.env.SupportedOs;

public class Os {

	public final static String LINUX_NEW_LINE = "\n";
	public final static String LINUX_PATH_SEPARATOR = "/";
	public final static String LINUX_PRE_COMMAND = "";
	public final static String LINUX_CLI_EXTENTION = ".sh";
	
	public final static String WINDOWS_NEW_LINE = "\r\n";
	public final static String WINDOWS_PATH_SEPARATOR = "\\";
	public final static String WINDOWS_PRE_COMMAND = "cmd /c ";
	public final static String WINDOWS_CLI_EXTENTION = ".bat";
	
	private static final SupportedOs os = resolveOs();
	
	public static String getNewLine() {
		if (os == SupportedOs.LINUX)
			return LINUX_NEW_LINE;
		return WINDOWS_NEW_LINE;
	}
	
	public static String getPathSeparator() {
		if (os == SupportedOs.LINUX)
			return LINUX_PATH_SEPARATOR;
		return WINDOWS_PATH_SEPARATOR;
	}
	
	public static SupportedOs getOs() {
		return os;
	}
	
	public static String getPreCommand() {
		if (os == SupportedOs.LINUX)
			return LINUX_PRE_COMMAND;
		return WINDOWS_PRE_COMMAND;
	}
	
	public static String getCliExtention() {
		if (os == SupportedOs.LINUX)
			return LINUX_CLI_EXTENTION;
		return WINDOWS_CLI_EXTENTION;
	}
		
	private static SupportedOs resolveOs() {
		switch(File.separator) {
		case LINUX_PATH_SEPARATOR:
			return SupportedOs.LINUX;
		case WINDOWS_PATH_SEPARATOR:
		default:
			return SupportedOs.WINDOWS;
		}
	}
}
