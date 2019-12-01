package migration.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import common.OperationSystem;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class MigrationFilesTest {

	@Test
	@Parameters(method = "dataLoadFilesFromDirTreeAndClasspath")
	public void testLoadFilesFromDirTreeAndClasspath(String folder, boolean isInClasspath, String dir) throws IOException {
		MigrationFiles f = new MigrationFiles(folder);
		assertEquals(Arrays.asList("First.class" , "Second.sql"), f.getFiles());
		assertEquals(isInClasspath, f.isInClasspath());
		assertNotEquals(-1, f.getDir().toString().indexOf(dir));
	}
	
	public Object[] dataLoadFilesFromDirTreeAndClasspath() {
		return new Object[] {
			new Object[] {
				"migration/files", true,
				String.format(
					"javainit%sji-database%sbin%smigration%sfiles",
					OperationSystem.PATH_SEPARATOR,
					OperationSystem.PATH_SEPARATOR,
					OperationSystem.PATH_SEPARATOR,
					OperationSystem.PATH_SEPARATOR
				)
			},
			new Object[] {
				"test/migration/files", false,
				String.format("test%smigration%sfiles", OperationSystem.PATH_SEPARATOR, OperationSystem.PATH_SEPARATOR)
			}
		};
	}

}
