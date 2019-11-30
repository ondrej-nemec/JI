package migration;

public class MigrationTool {

	public void migrate() throws Exception {
	/*	File file = getMigrationDir(folder);
		Map<String, String> map = loadFiles(file.listFiles());
		doMigrations(file, map, folder, false);*/
	}
	
	public void revert() {
		
	}
	
	public void revert(String id) {
		
	}
	
	public void revert(int steps) {
		
	}
	
	/*
	List<String> ids = ziskane z preparation
		URL[] urls = new URL[]{dir.toURI().toURL()};
		// each migration
		try (URLClassLoader loader = new URLClassLoader(urls);) {
			for (Object key : files.keySet()) { projit od 0 do n nebo opacne podle revert a kontrola id migrace
				zavolani single
			}
		}
	*/
}
