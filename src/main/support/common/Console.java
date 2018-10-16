package common;

import java.io.InputStream;
import java.util.Scanner;

public class Console {

	private final Scanner scanner;	
	
	public Console() {
		this.scanner = new Scanner(System.in);
	}
	
	protected Console(InputStream stream) {
		this.scanner = new Scanner(stream);
	}
	
	public synchronized void out(String message) {
		System.out.println(message);
	}
	
	public synchronized void err(String message) {
		System.err.println(message);
	}
	
	public synchronized String in() {
		return scanner.nextLine();
	}	
}
