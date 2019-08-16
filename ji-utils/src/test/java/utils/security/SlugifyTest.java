package utils.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import utils.security.Slugify;

@RunWith(JUnitParamsRunner.class)
public class SlugifyTest {

	@Test
	@Parameters
	public void testTuSlugSlugifyString(String string, String expectedSlug) {
		assertEquals(expectedSlug, Slugify.toSlug(string));
	}
	
	public Object[] parametersForTestTuSlugSlugifyString() {
		return new Object[] {
			new Object[] {
				"text",
				"text"
			},
			new Object[] {
				"Text Text",
				"text_text"
			},
			new Object[] {
				"šèøž",
				"scrz"
			},
		};
	}
	
}
