package ji.migration;

import java.time.LocalDateTime;
import java.util.Arrays;

import ji.common.Logger;
import ji.database.Database;
import ji.database.DatabaseConfig;

public class MigrationPathEndToEndTest {

	public static void main(String[] args) {
		System.out.println("start");
		DatabaseConfig config = new DatabaseConfig(
				"postgresql", "//localhost", true, "paths", 
				"postgres", "1234", Arrays.asList(
					"migration/path_files_src",
					"migration/path_files_res",
					"test/path_files_dir"
				), 5
			);
		Database db = new Database(config, getLogger());
		db.createDbAndMigrate();
		System.out.println("end");
	}
	private static Logger getLogger() {
		return new Logger() {

		    public void print(String severity, Object message) {
				System.out.println(LocalDateTime.now() + " " + severity + " " + message.toString());
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
