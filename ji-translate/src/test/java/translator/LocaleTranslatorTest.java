package translator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import common.Logger;

public class LocaleTranslatorTest {

	@Test
	public void testTranslateSimple() {
		LocaleTranslator translator = new LocaleTranslator(
			new LanguageSettings("", Arrays.asList()),
			Arrays.asList("modules", "modules2", "langs"),
			Mockito.mock(Logger.class)
		);
		assertEquals("Common text", translator.translate("common.some.key"));
		assertEquals("Common text 2", translator.translate("common.some2.key"));
		assertEquals("Default", translator.translate("messages.key"));
	}
/*
	@Test
	public void testTranslateMoreLanguages() {
		LocaleTranslator translator = new LocaleTranslator(
			new LanguageSettings("", Arrays.asList(
				new Locale("cs", true, Arrays.asList())
			)),
			Arrays.asList("modules", "modules2", "langs"),
			Mockito.mock(Logger.class)
		);
		assertEquals("common.some.key", translator.translate("common.some.key"));
		assertEquals("common.some2.key", translator.translate("common.some2.key"));
		assertEquals("Default", translator.translate("messages.key"));
	}
*/	
}