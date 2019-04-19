package translator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ResourceBundle;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import common.ILogger;

@RunWith(JUnitParamsRunner.class)
public class TranslatorTest {
	
	private Translator translator;
	
	private ILogger logger;
	
	public TranslatorTest() {
		this.logger = mock(ILogger.class);
		translator = new Translator(ResourceBundle.getBundle("messages"), logger);
	}
	
	@Test
	public void testTranslateNotExistingKey(){
		assertEquals("not-existing-key", translator.translate("not-existing-key"));
		verify(logger).warn("Missing key - {key=not-existing-key, ResourceBundleName=messages, name=default}");
	}
	
	@Test
	public void testTranslateCountNotExistingCount(){
		assertEquals("translate.works : 1", translator.translate("translate.works", 1));
		verify(logger).warn("Missing count: 1; {key=translate.works, ResourceBundleName=messages, name=default,[1,]}");
	}
	
	@Test
	public void testTranslateWorks(){
		assertEquals(
				"Translated message",
				translator.translate("translate.works")
			);
	}
	
	@Test
	public void testTranslateFromWorks(){
		translator.addResource("from", ResourceBundle.getBundle("from"));
		assertEquals(
				"Translated message from",
				translator.translateFrom("from", "translate.from.works")
			);
	}
	
	@Test(expected = RuntimeException.class)
	public void testTranslateWithVariableThrowWhenVariableDontExist(){
		translator.translate("too.many.variables", "var");
	}
	
	@Test
	public void testTranslateWithVariablesMoreVariablesLogged() {
		assertEquals(
					"Variable: var",
					translator.translate(
							"test.one.variable",
							"var",
							"var2"
						)
				);
		verify(logger).info("More variables given: 2; "
				+ "{key=test.one.variable, ResourceBundleName=messages, name=default,[var,var2,]}");
	}
	
	@Test
	@Parameters
	public void testTranslateWithVariableWorks(String expectedMessage, String key, char start, char end, String... variables){
		if (start != '\u0000' && end != '\u0000')
			translator.setVariableSeparators(start, end);
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
					"Variable: var", "test.one.variable", '\u0000', '\u0000', "var"
				},
				new Object[]{
					"Variables: 4, four", "test.two.variables.%%", '\u0000', '\u0000', new Integer(4).toString(), "four"
				},
				new Object[]{
					"Variables: varA, varB", "test.two.variables.<>", '<', '>', "varA", "varB"
				}
		};
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
}
