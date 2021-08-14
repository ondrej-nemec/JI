package query;

import query.wrappers.AlterTableBuilder;
import query.wrappers.CreateTableBuilder;
import query.wrappers.CreateViewBuilder;
import query.wrappers.DeleteBuilder;
import query.wrappers.ExecuteBuilder;
import query.wrappers.InsertBuilder;
import query.wrappers.SelectBuilder;
import query.wrappers.UpdateBuilder;
import querybuilder.Functions;

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
