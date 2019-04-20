package translator;

import java.io.Serializable;

class Info implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String bundleName;
	private String key;
	private String[] variables;
	
	public Info(String name, String bundleName, String key) {
		super();
		this.name = name;
		this.bundleName = bundleName;
		this.key = key;
	}
	
	public Info(String name, String bundleName, String key, String[] variables) {
		super();
		this.name = name;
		this.bundleName = bundleName;
		this.key = key;
		this.variables = variables;
	}
	
	public Info(String name, String bundleName, String key, int count) {
		super();
		this.name = name;
		this.bundleName = bundleName;
		this.key = key;
		this.variables = new String[]{new Integer(count).toString()};
	}
	
	public String getName() {
		return name;
	}
	
	public String getBundleName() {
		return bundleName;
	}
	
	public String getKey() {
		return key;
	}
	
	public String[] getVariables() {
		return variables;
	}
	
	@Override
	public String toString() {
		String result ="{key=" + key + ", ResourceBundleName=" + bundleName + ", name=" + name;
		if(variables != null){
			result += ",[";
			for (String s : variables) {
				result += s + ",";
			}
			result += "]";
		}
			
		return result + "}";
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Info))
			return false;
		Info i = (Info)o;
		if(!name.equals(i.name))
			return false;
		if(!bundleName.equals(i.bundleName))
			return false;
		if(!key.equals(i.key))
			return false;
		if(variables.length != i.variables.length)
			return false;
		for(int a = 0; a < i.variables.length; a++){
			if(!variables[a].equals(i.variables[a]))
				return false;
		}
		return true;
	}
}
