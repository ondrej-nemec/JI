package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
	
	public static String format(String pattern) {
		return format(pattern, LocalDateTime.now());
	}
	
	public static String format(String pattern, LocalDateTime date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return date.format(format);
	}

}
