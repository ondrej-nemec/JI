package database;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import common.Logger;
import common.functions.DateTime;
import querybuilder.ColumnType;
import querybuilder.SelectQueryBuilder;

public class DatabaseGeneralQueryTesting {

	public static void main(String[] args) throws SQLException {
		List<String> migrationPath = Arrays.asList("test/database");
		String dbName = "toti2";
		List<DatabaseConfig> configs = Arrays.asList(
			//*
			new DatabaseConfig(
				"mysql", "//localhost", true, dbName, 
				"root", "", migrationPath, "Europe/Prague", 5
			),
			//*/
			//*
			new DatabaseConfig(
				"derby", "C:\\software\\DerbyDB\\bin", false, dbName, 
				"root", "", migrationPath, "Europe/Prague", 5
			),
			//*/
			//*	
			new DatabaseConfig(
				"postgresql", "//localhost", true, dbName, 
				"postgres", "1234", migrationPath, "Europe/Prague", 5
			)
			//*/
		);
				
		Map<String, Object> filters = new HashMap<>();
		filters.put("id", 10);
	//	filters.put("name", null); // "Name #0"
		filters.put("description", "Desc #0");
	//	filters.put("is_active", true);
		Map<String, Object> sorting = new HashMap<>();
		sorting.put("name", "ASC");
		sorting.put("description", "Desc");
		sorting.put("id", "DESC");
		
		for (DatabaseConfig config : configs) {
			System.out.println("Database " + config.type);
			try {
				Database database = new Database(config, getLogger());
				database.createDbAndMigrate();
				System.out.println("Migrated");
				//*
				List<Map<String, Object>> data = database.applyBuilder((builder)->{
					List<Map<String, Object>> items = new LinkedList<>();
					SelectQueryBuilder select = builder.select("*").from("query_test");
					select.where("1=1");
					select.addParameter(":empty", "");
					filters.forEach((filter, value)->{
						String where = "";
						if (value == null) {
							where = filter + " is null";
						} else if (value.toString().length() > 20) {
							where = builder.getSqlFunctions().concat(":empty", filter)
									+ " like :" + filter + "LikeValue"
									+ " OR " + filter + " = :" + filter + "Value";
						} else {
							// this is fix for derby DB - integer cannot be concat or casted to varchar only on char
							where = builder.getSqlFunctions().cast(filter, ColumnType.charType(20))
									+ " like :" + filter + "LikeValue"
									+ " OR " + filter + " = :" + filter + "Value";
						}
						select.andWhere(where)
						.addParameter(":" + filter + "LikeValue", value + "%")
						.addParameter(":" + filter + "Value", value);
					});
					StringBuilder orderBY = new StringBuilder();
					sorting.forEach((sort, direction)->{
						if (!orderBY.toString().isEmpty()) {
							orderBY.append(", ");
						}
						orderBY.append(sort + " " + direction);
					});
					if (!sorting.isEmpty()) {
						select.orderBy(orderBY.toString());
					}
					System.out.println(select.createSql());
					select.fetchAll().forEach((row)->{
						items.add(row.getValues());
					});
					return items;
				});
				System.out.println("Data:");
				data.forEach((row)->System.out.println("  " + row));
				//*/
				
				System.out.println("Database finish");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
