package migration.resources;

import java.io.File;
import java.util.List;

import common.exceptions.NotImplementedYet;

public class MigrationFiles {
		
	public MigrationFiles(String folder) {
		// TODO Auto-generated constructor stub
	}
	
	public File getDir() {
		throw new NotImplementedYet();
	}
	
	public boolean isInClasspath() {
		throw new NotImplementedYet();
	}
	
	public List<String> getFiles() {
		throw new NotImplementedYet();
	}
/*
	protected File getMigrationDir(String folder) throws IOException {
		try {
			File file = getResourceFolderFiles(folder);
			if (file.listFiles() != null) {
				external = false;
				return file;
			}
		} catch (Exception e) {  ignored  }
		
		try {
			File file = getFolderFiles(folder);
			if (file.listFiles() != null) {
				external = true;
				return file;
			}
		} catch (Exception e) {  ignored  }
		
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
	
	
	
	protected Map<String, String> loadFiles(File[] files) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		Map<String, String> loadedFiles = new TreeMap<>();
		for (File file : files) {
			FileExtension fe = new FileExtension(file.getName());
			loadedFiles.put(fe.getName(), fe.getExtension());
			
			switch (fe.getExtension()) {
				case "sql": break;
				case "class": break;
				case "java":
					compiler.run(null, null, null, file.getPath()); // streamy, kam se zapisuje
					break;
				default: break;
			}
		}
		return loadedFiles;
	}
	*/
}
