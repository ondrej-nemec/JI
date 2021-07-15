package translator;

import java.util.Arrays;
import java.util.HashMap;

public class LocalTranslatorEndToEndTest {

	public static void main(String[] args) {
		LocaleTranslator translator = new LocaleTranslator(
			new LanguageSettings("", Arrays.asList(
				new Locale("cs", true, Arrays.asList("cs_CZ")),
				new Locale("ru", true, Arrays.asList()),
				new Locale("zh", true, Arrays.asList()),
				new Locale("en", true, Arrays.asList())
			)),
			Arrays.asList("modules", "modules2", "langs"),
			new LoggerImpl()
		);
		
		System.out.println(translator.translate("common.some.key"));
		System.out.println(translator.translate("common.some2.key"));
		System.out.println(translator.translate("common.some.key"));
		System.out.println(translator.translate("common.some2.key"));
		System.out.println("---------");
		System.out.println(translator.translate("messages.key", new HashMap<>(), "cs"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "cs_CZ"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "en"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "ru"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "zh"));
		System.out.println(translator.translate("messages.key"));
		System.out.println("---------");
		System.out.println(translator.translate("messages.key", new HashMap<>(), "cs"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "cs_CZ"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "en"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "ru"));
		System.out.println(translator.translate("messages.key", new HashMap<>(), "zh"));
		System.out.println(translator.translate("messages.key"));
		
	}
	
}
