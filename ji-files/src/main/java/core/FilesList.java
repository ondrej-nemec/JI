package core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FilesList {

	private URL url;
	private final List<String> files;

	public static FilesList get(String folder, boolean recursive) throws Exception {
		return new FilesList(folder, recursive);
	}

	private FilesList(String folder, boolean recursive) throws Exception {
		this.files = getFiles(folder, recursive);
	}
	/***************/
	public List<String> getFiles() {
		return files;
	}
	
	public URL getURL() {
		return url;
	}
	/**************/
	private List<String> getFiles(String folder, boolean recursive) throws Exception {
		URL url = Thread.currentThread().getClass().getResource("/" + getClass().getCanonicalName().replaceAll("\\.", "/") + ".class");
		
		// rsrc - resources - export
		// jar - in separated jar - gradle build
		if (url.toString().startsWith("rsrc:") || url.toString().startsWith("jar:")) {
			this.url = getClass().getResource("/" + folder);
		    return jar(folder, recursive);
		} else if (url.toString().startsWith("file:")) {
		    return dirTree(folder, recursive);
		} else {
		    throw new IOException("Folder not foud neither in classpath neider in dir tree: " + url);
		}
	}

     /*******************/
     
     private List<String> jar(String expectedNamespace, boolean recursive) throws Exception {
         List<String> files = new LinkedList<>();
         for (URL url : new URL[] {ClassLoader.getSystemResource(expectedNamespace)}) {
              JarURLConnection connection = (JarURLConnection) url.openConnection();
              JarFile file = connection.getJarFile();
              Enumeration<JarEntry> entries = file.entries();
              List<String> dirs = new LinkedList<>();
              while (entries.hasMoreElements()) {
                  JarEntry e = entries.nextElement();
                  if (e.getName().startsWith(expectedNamespace)) {
                	  if (e.getName().endsWith("/")) {
                		  if (!recursive) {
                			  dirs.add(e.getName());
                		  }
                	  } else {
                		  if (recursive || !dirs.contains(e.getName())) {
                			  files.add(e.getName().replace(expectedNamespace + "/", ""));
                		  }
                	  }
                  }
              }
         }
         return files;
     }

     /*************/

     private List<String> dirTree(String folder, boolean recursive) throws Exception {
    	folder = folder.startsWith("/") ? folder.substring(1) : folder;
    	ClassLoader loader = getClass().getClassLoader();
	    URL url = loader.getResource(folder);
	    if (url == null) {
	    	File f = new File(folder);
	    	this.url = f.toURI().toURL();
	    	List<String> files = new LinkedList<>();
		    addFileName(files, f, f.getAbsolutePath() + File.separator, recursive);
		    return files; 
	    }
	    String path = url.getPath();
	    File dir = new File(path);
	    this.url = dir.toURI().toURL();
	    List<String> files = new LinkedList<>();
	    addFileName(files, dir, dir + File.separator, recursive);
	    return files;
     }

     private void addFileName(List<String> files, File dir, String replacement, boolean recursive) {
         for (File f : dir.listFiles()) {
              if (f.isDirectory()) {
            	  if (recursive) {
            		  addFileName(files, f, replacement, recursive);
            	  }
              } else {
                  files.add(f.getAbsolutePath().replace(replacement, "").replaceAll("\\\\", "/"));
              }
         }
     }

} 
