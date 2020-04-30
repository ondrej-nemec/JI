package utils.io;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class PropertiesLoaderTest {

	@Test(expected = IOException.class)
	public void testConstructorForFilesThrowIfNoFileInDir() throws FileNotFoundException, IOException {
		PropertiesLoader.loadProperties("env/not-existing.properties");
	}

	@Test
	@Parameters({"conf","env"})
	public void testConstructorWorks(String path) throws FileNotFoundException, IOException {
		Properties prop = PropertiesLoader.loadProperties(path + "/app.properties");
		assertEquals("dev", prop.get("app.mode"));
	}

}
