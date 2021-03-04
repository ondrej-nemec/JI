package database;

import java.sql.SQLException;
import java.util.Arrays;

import common.Logger;
import common.functions.DateTime;

public class DatabaseTransactionTesting {

	public static void main(String[] args) throws SQLException {
		DatabaseConfig config = new DatabaseConfig(
				"mysql", 
				"//localhost", 
				true,
				"toti", 
				"root", 
				"",
				Arrays.asList(),
				"Europe/Prague",
				5
		);
		Database db = new Database(config, getLogger());
		System.out.println("start");
		db.applyQuery((con)->{
			con.setAutoCommit(false);
			try {
				con.setAutoCommit(false);
				con.prepareStatement("create table a(id int)").execute();
				con.commit();
				con.setAutoCommit(false);
				con.prepareStatement("insert into a(id) values(1)").execute();
				con.commit();
				con.setAutoCommit(false);
				con.prepareStatement("insert into a(id) values(2)").execute();
				con.rollback();
			} catch (Exception e) {
				con.rollback();
				throw e;
			}
			con.commit();
			return null;
		});
		System.out.println("end");
	}

	private static Logger getLogger() {
		return new Logger() {

		    public void print(String severity, Object message) {
				System.out.println(DateTime.format("yyyy-mm-dd H:mm:ss") + " " + severity + " " + message.toString());
			}
			
			public void print(String severity, Object message, Throwable t) {
				/*
				print(severity, message.toString() + ", " + t.getMessage());
				/*/
				print(severity, message.toString());
				t.printStackTrace();
				//*/
			}

			@Override
			public void debug(Object message) {
				print("DEBUG", message);
			}

			@Override
			public void debug(Object message, Throwable t) {
				print("DEBUG", message, t);
			}

			@Override
			public void info(Object message) {
				print("INFO", message);
			}

			@Override
			public void info(Object message, Throwable t) {
				print("INFO", message, t);
			}

			@Override
			public void warn(Object message) {
				print("WARN", message);
			}

			@Override
			public void warn(Object message, Throwable t) {
				print("WARN", message, t);
			}

			@Override
			public void error(Object message) {
				print("ERROR", message);
			}

			@Override
			public void error(Object message, Throwable t) {
				print("ERROR", message, t);
			}

			@Override
			public void fatal(Object message) {
				print("FATAL", message);
			}

			@Override
			public void fatal(Object message, Throwable t) {
				print("FATAL", message, t);
			}

			@Override
			public void trace(Object message) {
				print("TRACE", message);
			}

			@Override
			public void trace(Object message, Throwable t) {
				print("TRACE", message, t);
			}

		} ;
	}
	
}
