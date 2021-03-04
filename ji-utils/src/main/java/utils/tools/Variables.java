package utils.tools;

import common.functions.Console;

public class Variables {
	
	public Variables() {
		Console c = new Console();
		
		c.out("System properties");
		for(Object o : System.getProperties().keySet()) {
			c.out(o.toString() + ": " + System.getProperty(o.toString()));
		}
		
		c.out("\nSystem env");
		for(String k : System.getenv().keySet()) {
			c.out(k + ": " + System.getenv(k));
		}
	}
	
	public static void main(String[] args) {
		new Variables();
	}
}
