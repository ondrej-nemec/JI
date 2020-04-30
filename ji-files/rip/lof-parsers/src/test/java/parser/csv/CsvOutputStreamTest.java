package parser.csv;

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

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import parser.csv.CsvOutputStream;

@RunWith(JUnitParamsRunner.class)
public class CsvOutputStreamTest {
	
	@Test
	@Parameters
	public void testWriteValueWorks(String write, String expected) throws IOException {
		OutputStream os = mock(OutputStream.class);
		CsvOutputStream format = new CsvOutputStream(os);
		
		format.writeValue(write);
		verify(os).write(expected.getBytes());		
	}
	
	public Collection<List<Object>> parametersForTestWriteValueWorks() {
		return Arrays.asList(
			Arrays.asList("text", "text"),
			Arrays.asList("text, text", "\"text, text\""),
			Arrays.asList("\"\"", "\"\"\"\""),
			Arrays.asList("text \n text","\"text \n text\"")			
		);
	}
	
	@Test
	public void testWriteNewLineWorks() throws IOException {
		String newLine = File.separator == "/" ? "\n" : "\r\n";
		OutputStream os = mock(OutputStream.class);
		CsvOutputStream format = new CsvOutputStream(os);
		
		format.writeNewLine();
		verify(os).write(newLine.getBytes());
	}
	
	@Test
	public void testWriteSimpleTextWorks() throws IOException {
		String newLine = File.separator == "/" ? "\n" : "\r\n";
		String a00 = "aaaa";
		String a01 = "aabb";
		String a10 = "bbbb";
		String a11 = "bbaa";
		
		OutputStream os = mock(OutputStream.class);
		CsvOutputStream format = new CsvOutputStream(os);
		
		format.writeValue(a00);
		format.writeValue(a01);
		format.writeNewLine();
		format.writeValue(a10);
		format.writeValue(a11);
		
		verify(os).write(a00.getBytes());
		verify(os).write(("," + a01).getBytes());
		verify(os).write(newLine.getBytes());
		verify(os).write(a10.getBytes());
		verify(os).write(("," + a11).getBytes());
	}
	
	@Test
	public void endToEndTest() {
		String file = getClass().getResource("/parser/csv-output.csv").getFile();
		try (OutputStream os = new FileOutputStream(file)) {
			CsvOutputStream format = new CsvOutputStream(os);
			String newLine = File.separator == "/" ? "\n" : "\r\n";
						
			format.writeValue("simple text");
			format.writeValue("\"text in quotes\"");
			format.writeValue("text,with,colons");
			format.writeNewLine();
			format.writeValue("\\\"");
			format.writeValue("\"quotes\"");
			format.writeValue("new" + newLine + "line");
			format.writeValue("");
			format.writeValue("10000");
			format.writeNewLine();
			format.writeValue("'single");
			format.writeValue(" quotes'");
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}	
}
