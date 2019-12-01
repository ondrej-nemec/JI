package migration.resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import common.FileExtension;
import common.structures.Tuple2;

public class MigrationFiles {
	
	private final File dir;
	private final boolean isInClasspath;
	private final List<String> files;
		
	public MigrationFiles(String folder) throws IOException {
		Tuple2<File, Boolean> aux = getMigrationDir(folder);
		this.dir = aux._1();
		this.isInClasspath = aux._2();
		this.files = loadFiles(dir.listFiles());
	}
	
	/***************************/
	
	public File getDir() {
		return dir;
	}
	
	public boolean isInClasspath() {
		return isInClasspath;
	}
	
	public List<String> getFiles() {
		return files;
	}
	
	/***************************/

	private Tuple2<File, Boolean> getMigrationDir(String folder) throws IOException {
		try {
			File file = getResourceFolderFiles(folder);
			if (file.listFiles() != null) {
				return new Tuple2<>(file, true);
			}
		} catch (Exception e) { /* ignored */ }
		
		try {
			File file = getFolderFiles(folder);
			if (file.listFiles() != null) {
				return new Tuple2<>(file, false);
			}
		} catch (Exception e) { /* ignored */ }
		
		throw new IOException("No folder founded: " + folder);
	}
	
	private File getResourceFolderFiles(String folder) {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    URL url = loader.getResource(folder);
	    String path = url.getPath();
	    return new File(path);
	}
	
	private File getFolderFiles(String folder) {
		return new File(folder);
	}

	private List<String> loadFiles(File[] files) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		List<String> loadedFiles = new LinkedList<>();
		for (File file : files) {
			FileExtension fe = new FileExtension(file.getName());
			switch (fe.getExtension()) {
				case "java":
					if (!loadedFiles.contains(fe.getName() + ".class")) {
						compiler.run(null, null, null, file.getPath()); // streamy, kam se zapisuje
						loadedFiles.add(fe.getName() + ".class");
					}
					break;
				case "sql":
				case "class": 
					loadedFiles.add(file.getName());
					break;
				default: break;
			}
		}
		return loadedFiles;
	}

}
