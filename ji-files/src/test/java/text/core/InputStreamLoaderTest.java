package text.core;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import core.text.InputStreamLoader;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class InputStreamLoaderTest {

	@Test(expected = FileNotFoundException.class)
	public void testConstructorForFilesThrowIfNoFileInDir() throws IOException {
		InputStreamLoader.createInputStream(getClass(), "----/not-existing.properties");
	}

	@Test
	@Parameters({"text/core","tests/core"})
	public void testConstructorForFilesWorksForClasspathAndPathOnly(String path) throws IOException {
		InputStreamLoader.createInputStream(getClass(), path + "/app.properties");
		assertTrue(true);
	}

}