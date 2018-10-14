package copy;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Cloner {
	
	public Cloner(String dirName, String appName) {		
		String app = System.getenv("APPDATA") + File.separator + appName;
		System.out.println(app);
		
		File a = new File(app);
		boolean exist = a.exists();
		boolean dir = a.isDirectory();
		
		if (!exist || !dir) {  //TODO overeni prav zapisu
			/*boolean succ = */ a.mkdir();
		}
		
		cloneDir("res" + File.separator + dirName , app+File.separator+dirName);

		
	}


	public void cloneDir(String source, String destination) {
		File srcDir = new File(source);
		File tgrDir = new File(destination);
		
		try {
			FileUtils.copyDirectory(srcDir, tgrDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Cloner("KVSO-program", "test");
	}

}
