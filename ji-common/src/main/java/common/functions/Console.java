package common.functions;

import java.util.Scanner;

public class Console {

	private final Scanner scanner;	
	
	public Console() {
		this.scanner = new Scanner(System.in);
	}
	
	public synchronized void out(final Object message) {
		System.out.println(message);
	}
	
	public synchronized void out() {
		out("");
	}
	
	public synchronized void out(final Object... messages) {
		for(Object message : messages) {
			out(message);
		}
	}
	
	public synchronized void err(final Object message) {
		System.err.println(message);
	}
	
	public synchronized String in() {
		return scanner.nextLine();
	}
	
	public synchronized String in(String query) {
		out(query);
		return in();
	}
	
	public synchronized void clear() {
		System.out.print("\033[H\033[2J");  
		System.out.flush();
	}
}
