package translator;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import common.Logger;

public class LocaleTranslatorTest {

	@Test
	public void testTranslate() {
		LocaleTranslator translator = new LocaleTranslator(
			new LanguageSettings("", Arrays.asList()),
			Arrays.asList("modules", "modules2"),
			Mockito.mock(Logger.class)
		);
		assertEquals("Common text", translator.translate("common.some.key"));
		assertEquals("Common text 2", translator.translate("common.some2.key"));
	}
	
}
