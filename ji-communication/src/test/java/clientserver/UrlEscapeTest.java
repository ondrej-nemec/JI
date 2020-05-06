package clientserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UrlEscapeTest {
	
	private final String unEscapedString = "a?b/c\\\\d:e f=g&h%i+j\"k*l'm";
	private final String escapedString = "a%3Fb%2Fc%5C%5Cd%3Ae+f%3Dg%26h%25i%2Bj\"k*l%27m";
	
	@Test
	public void testUnEscapeTextReplacePercentages() {
		assertEquals(unEscapedString, UrlEscape.unEscapeText(escapedString));
	}
	
	@Test
	public void testEscapeTextReplacePercentages() {
		assertEquals(escapedString, UrlEscape.escapeText(unEscapedString));
	}
}
		