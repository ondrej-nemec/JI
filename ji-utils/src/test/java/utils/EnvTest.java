package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import utils.enums.AppMode;

@RunWith(JUnitParamsRunner.class)
public class EnvTest {

	@Test(expected = IOException.class)
	public void testConstructorForFilesThrowIfNoFileInDir() throws FileNotFoundException, IOException {
		new Env("env/not-existing.properties");
	}
	
	@Test
	@Parameters({"conf","env"})
	public void testConstructorForFilesWorksForClasspathAndPathOnly(String path) throws FileNotFoundException, IOException {
		new Env(path + "/app.properties");
		assertTrue(true);
	}
	
	@Test
	@Parameters
	public void testConstructorForFileFindCorrectProperties(final AppMode mode, final String subDir)
			throws FileNotFoundException, IOException {
		Env e = new Env("env/env." + subDir + ".properties");
		assertEquals(mode, e.getAppMode());
		assertEquals("value", e.getProperties().getProperty("key"));
	}
	
	public Collection<Object[]> parametersForTestConstructorForFileFindCorrectProperties() {
		return Arrays.asList(
				new Object[] {AppMode.PROD, "prod"},
				new Object[] {AppMode.DEV, "dev"},
				new Object[] {AppMode.TEST, "test"}
		);
	}
	
	@Test(expected=RuntimeException.class)
	public void testConstructorForCodeThrowsIfAppModeIsNotSetted() {
		new Env(new Properties()).getAppMode();
	}

}
