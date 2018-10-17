package common.env;

import java.io.File;

public class Os {

	public final static String LINUX_NEW_LINE = "\n";
	public final static String LINUX_PATH_SEPARATOR = "/";
	
	public final static String WINDOWS_NEW_LINE = "\r\n";
	public final static String WINDOWS_PATH_SEPARATOR = "\\";
	
	private final SupportedOs os;
	
	public Os() {
		this.os = resolveOs();
	}
	
	public String getNewLine() {
		if (os == SupportedOs.LINUX)
			return LINUX_NEW_LINE;
		return WINDOWS_NEW_LINE;
	}
	
	public String getPathSeparator() {
		if (os == SupportedOs.LINUX)
			return LINUX_PATH_SEPARATOR;
		return WINDOWS_PATH_SEPARATOR;
	}
	
	public SupportedOs getOs() {
		return os;
	}
	
	private SupportedOs resolveOs() {
		switch(File.separator) {
		case LINUX_PATH_SEPARATOR:
			return SupportedOs.LINUX;
		case WINDOWS_PATH_SEPARATOR:
		default:
			return SupportedOs.WINDOWS;
	}
	}
}
