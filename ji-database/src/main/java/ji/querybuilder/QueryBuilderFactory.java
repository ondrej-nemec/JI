package ji.querybuilder;

import java.sql.Connection;

import ji.querybuilder.builders.AlterTableBuilder;
import ji.querybuilder.builders.CreateTableBuilder;
import ji.querybuilder.builders.CreateViewBuilder;
import ji.querybuilder.builders.DeleteBuilder;
import ji.querybuilder.builders.ExecuteBuilder;
import ji.querybuilder.builders.Functions;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.builders.UpdateBuilder;

public interface QueryBuilderFactory {
	
	 Functions getSqlFunctions();
	
	 DeleteBuilder delete(String table);
	
	 InsertBuilder insert(String table);
	
	 UpdateBuilder update(String table);
	
	 SelectBuilder select(String select);
		
	 ExecuteBuilder deleteTable(String table);
	
	 CreateTableBuilder createTable(String name);
	
	 AlterTableBuilder alterTable(String name);
	
	/***********************/
	
	 ExecuteBuilder deleteView(String table);
	
	 CreateViewBuilder createView(String name);
	
	 CreateViewBuilder alterView(String name);
	
	/***********************/
	
	 ExecuteBuilder createIndex(String name, String table, String... colums);
	
	 ExecuteBuilder deleteIndex(String name, String table);
	 
	 Connection getConnection();
	
}
