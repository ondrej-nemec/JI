package ji.common.functions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTime {
	
	public static String format(String pattern) {
		return format(pattern, LocalDateTime.now());
	}
	
	public static String format(String pattern, TemporalAccessor date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return format.format(date);// date.format(format);
	}
	
}
