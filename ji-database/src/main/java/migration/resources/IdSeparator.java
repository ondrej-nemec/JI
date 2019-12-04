package migration.resources;

import common.FileExtension;

public class IdSeparator {
	
	private final String id;
	
	private final String desc;
	
	public IdSeparator(String name, String separator) {
		if (!name.contains(separator)) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}
		String[] aux = new FileExtension(name).getName().split(separator);
		if (aux.length != 2) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}
		this.id = aux[0];
		this.desc = aux[1];
	}

	public String getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}
	
}
