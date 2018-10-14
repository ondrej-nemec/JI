package common;

import java.util.Scanner;

public class Console {

	private static Scanner scanner = new Scanner(System.in);
	
	public synchronized static void out(String message) {
		System.out.println(message);
	}
	
	public synchronized static void err(String message) {
		System.err.println(message);
	}
	
	public synchronized static String in() {
		return scanner.nextLine();
	}	
}
