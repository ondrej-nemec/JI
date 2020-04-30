package parser.env;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import exceptions.ParserSyntaxException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class EnvOutputStreamTest {

	private final String NEW_LINE = File.separator == "/" ? "\n" : "\r\n";
	
	@Test
	@Parameters
	public void testWriteTwinsWorks(String key, String value, String message) throws IOException {
		OutputStream os = mock(OutputStream.class);
		
		EnvOutputStream env = new EnvOutputStream(os);
		
		env.writeTwins(key, value);
		
		verify(os, times(1)).write(message.getBytes());
	}
	
	public Collection<List<Object>> parametersForTestWriteTwinsWorks() {
		return Arrays.asList(
			Arrays.asList(
					"key",
					"value",
					"key=value" + NEW_LINE
				),
			Arrays.asList(
					"key",
					"new\nline",
					"key=\"new\\nline\"" + NEW_LINE
				),
			Arrays.asList(
					"key",
					"'new\nline'",
					"key=\"\\'new\\nline\\'\"" + NEW_LINE
				),
			Arrays.asList(
					"key",
					"",
					"key=" + NEW_LINE
				),
			Arrays.asList(
					"key",
					"\"value\"",
					"key=\"\\\"value\\\"\"" + NEW_LINE //key="\"value\""
				),
			Arrays.asList(
					"key",
					"'value'",
					"key=\"\\'value\\'\"" + NEW_LINE
				)
		);
	}
	
	@Test(expected=ParserSyntaxException.class)
	public void testParseThrowsWhenBackslashInKey() throws IOException {
		OutputStream os = mock(OutputStream.class);
		
		EnvOutputStream env = new EnvOutputStream(os);
		
		env.writeTwins("k\\ey", "");
	}	
	
	@Test(expected=ParserSyntaxException.class)
	public void testParseThrowsWhenNewLineInKey() throws IOException {
		OutputStream os = mock(OutputStream.class);
		
		EnvOutputStream env = new EnvOutputStream(os);
		
		env.writeTwins("k\ney", "");
	}
	
	@Test
	public void endToEndTest() {
		String file = getClass().getResource("/parser/env-output.env").getFile();
		try (OutputStream os = new FileOutputStream(file)) {
			EnvOutputStream env = new EnvOutputStream(os);
						
			env.writeTwins("key", "value");
			env.writeTwins("key", "new\nline");
			env.writeTwins("key", "'new\nline'");
			env.writeTwins("key", "");
			env.writeTwins("key", "\"value\"");
			env.writeTwins("key", "'value'");
		
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}	
}
