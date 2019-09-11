package translator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static common.MapInit.*;

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
	
	/******

	@Test
	public void testTranslateCountNotExistingCount(){
		assertEquals("translate.works : 1", translator.translate("translate.works", 1));
		verify(logger).warn("Missing count: 1; {key=translate.works, ResourceBundleName=messages, name=default,[1,]}");
	}
		
	@Test
	@Parameters
	public void testTranslateWithCountWorks(String expectedMessage, int count){
		assertEquals(
			expectedMessage,
			translator.translate(
				"test.count",
				count
			)
		);
	}
	
	public Object[] parametersForTestTranslateWithCountWorks() {
		return new Object[] {
			new Object[]{"Less", -3},
			new Object[]{"Negative", -1},
			new Object[]{"Zero value", 0},
			new Object[]{"Exactly: 1", 1},
			new Object[]{"Between: 3", 3},
			new Object[]{"Between: 4", 4},
			new Object[]{"Separator: 5", 5},
			new Object[]{"Separator: 6", 6},
			new Object[]{"More: 9", 9}
		};
	}
	
	*/
}
