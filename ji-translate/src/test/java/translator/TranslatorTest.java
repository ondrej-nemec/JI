package translator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import common.Logger;

@RunWith(JUnitParamsRunner.class)
public class TranslatorTest {
	
	@Test
	@Parameters({"mess-tree", "mess-classpath"})
	public void testTranslateLoadFilesFromClasspathAndDirTree(String resource) {
		new Translator(mock(Logger.class), "trans/", resource);
		assertTrue(true);
	}
	
	@Test
	public void testTranslateReturnsKeyIfKeyNotExists(){
		Translator translator = new Translator(mock(Logger.class), "", "messages");
		assertEquals("not-existing-key", translator.translate("not-existing-key"));
		assertEquals("not-existing-resource.translate.works", translator.translate("not-existing-resource.translate.works"));
	}
	
	@Test
	@Parameters(method = "dataTranslateReturnsCorrectKey")
	public void testTranslateReturnsCorrectKey(String key, String expected) {
		Translator translator = new Translator(mock(Logger.class), "", "messages", "from");
		assertEquals(expected, translator.translate(key));
	}
	
	public Object[] dataTranslateReturnsCorrectKey() {
		return new Object[] {
			new Object[] {
				"translate.works", "Translated message"
			},
			new Object[] {
				"translate-works", "Translated message"
			},
			new Object[] {
				"messages.translate.works", "Translated message"
			},
			new Object[] {
				"from.translate.from.works", "Translated message from"
			},
		};
	}
	
	@Test
	@Parameters
	public void testTranslateWithVariableWorks(String expectedMessage, String key, Map<String, String> variables){
		Translator translator = new Translator(mock(Logger.class), "", "messages");
		assertEquals(
			expectedMessage,
			translator.translate(
				key,
				variables
			)
		);
	}
	
	public Object[] parametersForTestTranslateWithVariableWorks() {
		return new Object[]{
			new Object[]{
				"Variable: var", "test.one.variable", hashMap(t("variable", "var"))
			},
			new Object[]{
				"Variables: 4, four", "test.two.variables", hashMap(t("a", 4), t("b", "four"))
			}
		};
	}

	@Test
	public void testTranslateCountNotExistingCount(){
		Translator translator = new Translator(mock(Logger.class), "", "messages");
		assertEquals("Translated message (1)", translator.translate("translate.works", 1));
	}
	
    @Test
    @Parameters
    public void testTranslateWithCountWorks(String expectedMessage, int count){
		Translator translator = new Translator(mock(Logger.class), "", "messages");
    	assertEquals(
    		expectedMessage,
    		translator.translate(
    			"test.count",
    			count,
    			hashMap(t("count", count))
    		)
    	);
    }
    
    public Object[] parametersForTestTranslateWithCountWorks() {
    	return new Object[] {
        	new Object[]{"Lower -20", -20},
    		new Object[]{"Lower -11", -11},
    		new Object[]{"Lower -10", -10},
    		new Object[]{"List 1", 1},
    		new Object[]{"List 2", 2},
    		new Object[]{"List 3", 3},
    		new Object[]{"Sequence -3", -3},
    		new Object[]{"Sequence -2", -2},
    		new Object[]{"Sequence -1", -1},
    		new Object[]{"Higher 11", 11},
    		new Object[]{"Higher 15", 15},
    		new Object[]{"Default 9", 9},
    		new Object[]{"Default 0", 0},
    		new Object[]{"Five", 5},
    	};
    }
    
    @Test
    @Parameters(method = "dataTranslateWorksWithSpecialAlphabets")
    public void testTranslateWorksWithSpecialAlphabets(String expected, Locale locale, String path) {
    	Locale.setDefault(locale);
    	Translator translator = new Translator(mock(Logger.class), path, "messages");    	
    	assertEquals(expected, translator.translate("key"));
    }
    
    public Object[] dataTranslateWorksWithSpecialAlphabets() {
    	return new Object[] {
    		new Object[] {"English text", Locale.ENGLISH, "langs/"},
    		new Object[] {"České znaky -> ěščřžýáíéůú", new Locale("cs"), "langs/"},
    		new Object[] {"中文文字", Locale.CHINESE, "langs/"},
    		new Object[] {"Русский текст", new Locale("ru"), "langs/"},
    		new Object[] {"Default", new Locale("not-existing"), "langs/"},

    		new Object[] {"English text", Locale.ENGLISH, "trans/langs/"},
    		new Object[] {"České znaky -> ěščřžýáíéůú", new Locale("cs"), "trans/langs/"},
    		new Object[] {"中文文字", Locale.CHINESE, "trans/langs/"},
    		new Object[] {"Русский текст", new Locale("ru"), "trans/langs/"},
    		new Object[] {"Default", new Locale("not-existing"), "trans/langs/"}
    	};
    }

}
