package migration.resources;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class MigrationFilesTest {

	@Test
	@Parameters(method = "dataLoadFilesFromDirTreeAndClasspath")
	public void testLoadFilesFromDirTreeAndClasspath(String folder, boolean isInClasspath, String dir, List<String> files) {
		MigrationFiles f = new MigrationFiles(folder);
		assertEquals(dir, f.getDir().toString());
		assertEquals(files, f.getFiles());
		assertEquals(isInClasspath, f.isInClasspath());
	}
	
	public Object[] dataLoadFilesFromDirTreeAndClasspath() {
		return new Object[] {
			new Object[] {
				
			},
			new Object[] {
				
			}
		};
	}
	
}
