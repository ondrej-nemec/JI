package common.functions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Deprecated
// TODO move to Dictionary value - getString
public class DateTime {
	
	public static String format(String pattern) {
		return format(pattern, LocalDateTime.now());
	}
	
	public static String format(String pattern, LocalDateTime date) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
		return date.format(format);
	}
	
	public static String format(String pattern, Calendar cal) {
		return DateTime.format("yyyy-MM-dd H:m:s", LocalDateTime.of(
                 cal.get(Calendar.YEAR),
                 cal.get(Calendar.MONTH)+1,
                 cal.get(Calendar.DAY_OF_MONTH),
                 cal.get(Calendar.HOUR_OF_DAY),
                 cal.get(Calendar.MINUTE),
                 cal.get(Calendar.SECOND)
        ));
	}

	public static String format(String pattern, int year, int month, int day, int hour, int minutes, int second) {
		return DateTime.format("yyyy-MM-dd H:m:s", LocalDateTime.of(year, month, day, hour, minutes, second));
	}
	
}
