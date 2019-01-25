package common;

import java.util.Scanner;

public class Console {

	private final Scanner scanner;	
	
	public Console() {
		this.scanner = new Scanner(System.in);
	}
	
	public synchronized void out(final String message) {
		System.out.println(message);
	}
	
	public synchronized void err(final String message) {
		System.err.println(message);
	}
	
	public synchronized String in() {
		return scanner.nextLine();
	}	
}
