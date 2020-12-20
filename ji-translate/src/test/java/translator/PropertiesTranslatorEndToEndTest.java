package translator;

import java.util.Locale;

import common.DateTime;
import common.Logger;

public class PropertiesTranslatorEndToEndTest implements Logger {

	public static void main(String[] args) {
		PropertiesTranslator t = PropertiesTranslator.create(new PropertiesTranslatorEndToEndTest(), "langs/messages");
		System.out.println(t.translate("key", new Locale("cs")));
		System.out.println(t.translate("key", new Locale("cs")));
		Translator t2 = t.withLocale(new Locale("en"));
		System.out.println(t2.translate("key"));
		System.out.println(t2.translate("key", new Locale("cs")));
		System.out.println(t.translate("key", new Locale("en")));
	}
	
	
    public void print(String severity, Object message) {
		System.out.println(DateTime.format("yyyy-mm-dd H:mm:ss") + " " + severity + " " + message.toString());
	}
	
	public void print(String severity, Object message, Throwable t) {
		/*
		print(severity, message.toString() + ", " + t.getMessage());
		/*/
		print(severity, message.toString());
		t.printStackTrace();
		//*/
	}

	@Override
	public void debug(Object message) {
		print("DEBUG", message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		print("DEBUG", message, t);
	}

	@Override
	public void info(Object message) {
		print("INFO", message);
	}

	@Override
	public void info(Object message, Throwable t) {
		print("INFO", message, t);
	}

	@Override
	public void warn(Object message) {
		print("WARN", message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		print("WARN", message, t);
	}

	@Override
	public void error(Object message) {
		print("ERROR", message);
	}

	@Override
	public void error(Object message, Throwable t) {
		print("ERROR", message, t);
	}

	@Override
	public void fatal(Object message) {
		print("FATAL", message);
	}

	@Override
	public void fatal(Object message, Throwable t) {
		print("FATAL", message, t);
	}

	@Override
	public void trace(Object message) {
		print("TRACE", message);
	}

	@Override
	public void trace(Object message, Throwable t) {
		print("TRACE", message, t);
	}

}
