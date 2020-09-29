package database;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Logger;
import database.Database;

public class ConnectionPoolEndToEndTest {

	public static void main(String[] args) {
		DatabaseConfig config = new DatabaseConfig(
				"mysql",
				"//localhost",
				true,
				"javainit_database_test",
				"root",
				"",
				Arrays.asList("migrations"),
				"Europe/Prague",
				2
		);
		
		Database d = new Database(config, new Logger() {
			
			@Override
			public void warn(Object message, Throwable t) {
				System.out.println(message + " " + t.getMessage());
			}
			
			@Override
			public void warn(Object message) {
				System.out.println(message);
			}
			
			@Override
			public void info(Object message, Throwable t) {
				System.out.println(message + " " + t.getMessage());
			}
			
			@Override
			public void info(Object message) {
				System.out.println(message);
			}
			
			@Override
			public void fatal(Object message, Throwable t) {
				System.out.println(message + " " + t.getMessage());
			}
			
			@Override
			public void fatal(Object message) {
				System.out.println(message);
			}
			
			@Override
			public void error(Object message, Throwable t) {
				System.out.println(message + " " + t.getMessage());
			}
			
			@Override
			public void error(Object message) {
				System.out.println(message);
			}
			
			@Override
			public void debug(Object message, Throwable t) {
				System.out.println(message + " " + t.getMessage());
			}
			
			@Override
			public void debug(Object message) {
				System.out.println(message);
			}

			@Override
			public void trace(Object message) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void trace(Object message, Throwable t) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ExecutorService exec = Executors.newFixedThreadPool(5);
		
		exec.execute(task(d, 1));
		exec.execute(task(d, 2));
		exec.execute(task(d, 3));
		exec.execute(task(d, 4));
		exec.execute(task(d, 5));
		
		try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
		print("END");
	}
	
	private static Runnable task(Database d, int i) {
		return ()->{
			print("Start task " + i);
			try {
				d.applyQuery((conn)->{
					print("Query running: " + i);
					try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
				});
			} catch (SQLException e) {
				print("ERROR " + i + ": " + e.getMessage());
			}
			print("End task " + i);
		};
	}
	
	private static void print(String message) {
		System.out.println(message);
	}

}
