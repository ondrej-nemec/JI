package copy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AppData {

	private final String appdata = System.getenv("APPDATA");
	
	/**
	 * vraci cestu k datum aplikace v app data
	 * @return
	 */
	public String getAppdata(final String appName){
		return appdata + "/" + appName;
	}
		
	/**
	 * zkontroluje, zda ma aplikace slozku v a app data
	 * pokud nema, vytvori ji
	 * @param appName
	 * @return
	 */
	public boolean checkAppDataDir(final String appName){
		if(!Files.isDirectory(Paths.get(getAppdata(appName))))
			return makeDir(getAppdata(appName));
		return false;
	}
	
	/**
	 * vytvoreni adresare
	 * @param path - cesta k novemu souboru
	 * @return
	 */
	public boolean makeDir(final String path){
		try {
			Files.createDirectory(Paths.get(path));
		} catch (IOException e) {
			System.err.println("Nelze vytvoøit adresáø: " + path + "\n" + e.getMessage());
			return false;
		}
		return true;
	}
	
	
	
	
	
}
