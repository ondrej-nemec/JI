package socketCommunication.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import socketCommunication.http.UrlEscape;

public class UrlEscapeTest {
	
	private final String unEscapedString = "a?b/c\\\\d:e f=g&h%i+j\"k*l'm{n}o[p]q<r>s";
	private final String escapedString = "a%3Fb%2Fc%5C%5Cd%3Ae+f%3Dg%26h%25i%2Bj%22k*l%27m%7Bn%7Do%5Bp%5Dq%3Cr%3Es";
	
	@Test
	public void testUnEscapeTextReplacePercentages() {
		assertEquals(unEscapedString, UrlEscape.unEscapeText(escapedString));
	}
	
	@Test
	public void testEscapeTextReplacePercentages() {
		assertEquals(escapedString, UrlEscape.escapeText(unEscapedString));
	}
}
