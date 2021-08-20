package querybuilder;

import querybuilder.builders.AlterTableBuilder;
import querybuilder.builders.CreateTableBuilder;
import querybuilder.builders.CreateViewBuilder;
import querybuilder.builders.DeleteBuilder;
import querybuilder.builders.ExecuteBuilder;
import querybuilder.builders.Functions;
import querybuilder.builders.InsertBuilder;
import querybuilder.builders.SelectBuilder;
import querybuilder.builders.UpdateBuilder;

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
	
}
