package translator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.Logger;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class PropertiesTranslatorTest {

	@Test
	public void testTranslateLoadFileFromClasspath() {
		PropertiesTranslator t = new PropertiesTranslator(mock(Logger.class), "trans/mess-classpath");
		assertEquals("Translated message", t.translate("mess-classpath.translate.works", new HashMap<>(), Locale.getDefault()));
	}

	@Test
	public void testTranslateLoadFileFromDirTree() {
		PropertiesTranslator t = new PropertiesTranslator(mock(Logger.class), "trans/mess-tree");
		assertEquals("Translated message", t.translate("mess-tree.translate.works", new HashMap<>(), Locale.getDefault()));
	}

	@Test
	@Parameters(method = "dataTranslateReplaceVariable")
	public void testTranslateReplaceVariable(String expected, String key, Map<String, String> params) {
		PropertiesTranslator t = new PropertiesTranslator(mock(Logger.class), "messages");
		assertEquals(expected, t.translate(key, params, Locale.getDefault()));
	}
	
	public Object[] dataTranslateReplaceVariable() {
		return new Object[] {
				new Object[] {
						"Translated message", "translate.works", hashMap()
					},
			new Object[] {
				"Translated message", "translate-works", hashMap()
			},
			new Object[] {
					"Variable: TEXT", "test.one.variable", hashMap(t("variable", "TEXT"))
				},
			new Object[] {
					"Variables: A, B", "test.two.variables", hashMap(t("a", "A"), t("b", "B"))
				}
		};
	}

	@Test
	@Parameters(method = "dataTranslateUseCorrectLocale")
	public void testTranslateUseCorrectLocale(String expected, Locale locale) {
		PropertiesTranslator t = new PropertiesTranslator(mock(Logger.class), "langs/messages");
		assertEquals(expected, t.translate("key", locale));
	}
	
	public Object[] dataTranslateUseCorrectLocale() {
		return new Object[] {
				new Object[] {
						"České znaky -> ěščřžýáíéůú", new Locale("cs")
					},
			new Object[] {
					"English text", new Locale("en")
			},
			new Object[] {
					"Русский текст", new Locale("ru")
				},
			new Object[] {
					"中文文字", new Locale("zh")
				},
			new Object[] {
					"Default", new Locale("missing")
				}
		};
	}

	@Test
	@Parameters(method = "dataTranslateSelectTransFileByPrefix")
	public void testTranslateSelectTransFileByPrefix(String key, String expected) {
		PropertiesTranslator t = new PropertiesTranslator(
				mock(Logger.class),
				"modules/common", 
				"modules/module", 
				"modules/messages"
		);
		assertEquals(expected, t.translate(key));
	}
	
	public Object[] dataTranslateSelectTransFileByPrefix() {
		return new Object[] {
			new Object[] {
				"common.some.key", "Common text"
			},
			new Object[] {
				"module.some.key", "Module text"
			},
			new Object[] {
					"some.key", "Default value"
				}
		};
	}
}
