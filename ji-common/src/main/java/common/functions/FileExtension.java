package common.functions;

public class FileExtension {
	
	private final String name;
	
	private final String extension;
	
	public FileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		
		if (i == -1) {
			this.name = fileName;
			this.extension = "";
		} else if (i == 0) {
			this.name = "";
			this.extension = fileName.substring(i+1);
		} else if (i > 0) {
			this.name = fileName.substring(0, i);
			this.extension = fileName.substring(i+1);
		} else {
			this.name = "";
			this.extension = "";
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getExtension() {
		return extension;
	}

}
