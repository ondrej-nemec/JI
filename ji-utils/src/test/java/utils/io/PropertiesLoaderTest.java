package utils.io;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

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
	public void testConstructorForFilesWorksForClasspathAndPathOnly(String path) throws FileNotFoundException, IOException {
		PropertiesLoader.loadProperties(path + "/app.properties");
		assertTrue(true);
	}

}
